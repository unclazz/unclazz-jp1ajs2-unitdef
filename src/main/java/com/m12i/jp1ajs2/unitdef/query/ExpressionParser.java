package com.m12i.jp1ajs2.unitdef.query;

import com.m12i.jp1ajs2.unitdef.parser.AbstractParser;
import com.m12i.jp1ajs2.unitdef.parser.ParseError;
import com.m12i.jp1ajs2.unitdef.parser.Reader;
import com.m12i.jp1ajs2.unitdef.parser.Result;

class ExpressionParser extends AbstractParser<Expression> {

	public Result<Expression> parse(final Reader in) {
		// 何はともあれ空白文字をスキップ
		parsers.skipWhitespace(in);
		if (in.hasReachedEof()) {
			// 空文字列もしくは空白文字のみからなるクエリの場合はエラー
			return Result.failure("Empty string.");
		}
		// それ以外の場合は再帰的にクエリを構文解析
		return parseExpression(in, true);
	}
	
	/**
	 * 式（論理演算・比較演算）をパースして返す.
	 * 再帰下降オプションは式のパースが「左結合」で実行されるようにするためのパラメータ。
	 * @param recursive 再帰下降オプション
	 * @return パースした式
	 * @throws ParseError 構文エラーが見つかった場合
	 */
	private Result<Expression> parseExpression(final Reader in, final boolean recursive) {
		// 再帰呼び出し時を想定してとにかく空白文字をスキップ
		parsers.skipWhitespace(in);
		// 現在文字をチェック
		if (in.current() == '!') {
			// 現在文字が"!"であれば単行論理演算子とみなして処理を進める
			in.next();
			parsers.skipWhitespace(in);
			// 単行論理演算子と右辺の式（これから再帰的にパースされる）をもとに論理演算オブジェクトを生成して返す
			final Result<Expression> expr0 = parseExpression(in, true);
			if (expr0.successful) {
				return Result.success(Expression.logical(Operator.NOT, expr0.value));
			} else {
				return expr0;
			}
		} else if(in.current() == '(') {
			// 現在文字が"("であれば結合方法を制御する丸括弧の開始とみなして処理を進める
			in.next();
			// 括弧の内側にあるのはともかく何かしらの式だからそれをパースする
			final Result<Expression> e = parseExpression(in, true);
			if (!e.successful) {
				return e;
			}
			// 式の後の空白文字をスキップ
			parsers.skipWhitespace(in);
			// 式の後には")"がつづく
			if (in.current() != ')') return Result.failure(')', in.current());
			in.next();
			// 再帰下降オプションをチェック
			if (!recursive) {
				// 再帰下降を許されていない場合は先ほど読み取った式を返す
				return e;
			} else {
				// 再帰下降を許されている場合は後続の論理演算（もしあれば）も含めて処理した結果を返す
				return parseLogical(in, e.value);
			}
		} else {
			// 現在文字が単行論理演算子でも丸括弧でもなかった場合は比較演算とみなして処理を進める
			// まず現在文字をチェックしてプロパティ記述をパースする
			final String prop = (in.current() == '"' || in.current() == '\'' 
					? parsers.parseQuotedString(in) : parseNonQuotedString(in)).value;
			// 空白文字をスキップ
			parsers.skipWhitespace(in);
			// 次に演算子をパースする
			final Result<Operator> op = parseComparativeOperator(in);
			if (!op.successful) {
				return Result.failure(op);
			}
			// 空白文字をスキップ
			parsers.skipWhitespace(in);
			// 演算子の種類をチェックして値の記述をパースする
			final Expression expr0;
			// プロパティ、演算子、値の3値から比較演算オブジェクトを構成する
			if (op.value == Operator.IS_NOT_NULL || op.value == Operator.IS_NULL) { 
				expr0 = Expression.comparative(Expression.property(prop), op.value, Expression.value(null));
			} else {
				final Result<String> value =  parseValue(in);
				if (!value.successful) return Result.failure(value);
				expr0 = Expression.comparative(Expression.property(prop), op.value, Expression.value(value.value));
			}
			// 再帰下降オプションをチェック
			if (!recursive) {
				// 再帰下降を許されていない場合は先ほど読み取った式を返す
				return Result.success(expr0);
			} else {
				// 再帰下降を許されている場合は後続の論理演算（もしあれば）も含めて処理した結果を返す
				return parseLogical(in, expr0);
			}
			
		}
	}
	
	/**
	 * 論理演算式をパースして返す.
	 * 引数として左辺にあたる式を受け取り、後続のクエリ文字列から論理演算子と右辺の式を読み取って、その3値から論理演算子式を構成して返す。
	 * 後続のクエリ文字列に論理演算子が見つからなかった場合は、引数として与えられた式をそのまま返す。
	 * @param left 論理演算の左辺
	 * @return パースした式
	 * @throws ParseError 構文エラーが見つかった場合
	 */
	private Result<Expression> parseLogical(final Reader in, final Expression left) {
		// まずは空白文字をスキップ
		parsers.skipWhitespace(in);
		// 現在文字をチェックして処理分岐
		final char c0 = in.current();
		if (c0 == 'a') {
			// 現在文字が"a"ならば"nd"が続かない限り構文エラー
			in.next();
			if (parsers.skipWord(in, "nd").failed) return Result.failure("Unknown operator.");
			in.next();
			parsers.skipWhitespace(in);
			// 引数として渡された式（左辺）と最前パースした演算子、
			// そしてこれからパースする式（右辺）で論理演算オブジェクトを構成して返す
			// ＊ただし右辺の式をパースするにあたり「右結合」にならないよう再帰下降オプションはOFFにする
			final Result<Expression> right = parseExpression(in, false);
			if (!right.successful) {
				return right;
			} else {
				return parseLogical(in, Expression.logical(left, Operator.AND, right.value));
			}
		} else if (c0 == '&') {
			// 現在文字が"&"ならば"&"が続かない限り構文エラー
			if (in.next() != '&') return Result.failure("Unknown operator.");
			in.next();
			parsers.skipWhitespace(in);
			// "and"の場合と同様に処理
			final Result<Expression> right = parseExpression(in, false);
			if (!right.successful) {
				return right;
			} else {
				return parseLogical(in, Expression.logical(left, Operator.AND, right.value));
			}
		} else if (c0 == 'o') {
			// 現在文字が"o"ならば"r"が続かない限り構文エラー
			if (in.next() != 'r') return Result.failure("Unknown operator.");
			in.next();
			parsers.skipWhitespace(in);
			// "and"の場合と同様に処理
			final Result<Expression> right = parseExpression(in, false);
			if (!right.successful) {
				return right;
			} else {
				return parseLogical(in, Expression.logical(left, Operator.OR, right.value));
			}
		} else if (c0 == '|') {
			// 現在文字が"|"ならば"|"が続かない限り構文エラー
			if (in.next() != '|') return Result.failure("Unknown operator.");
			in.next();
			parsers.skipWhitespace(in);
			// "and"の場合と同様に処理
			final Result<Expression> right = parseExpression(in, false);
			if (!right.successful) {
				return right;
			} else {
				return parseLogical(in, Expression.logical(left, Operator.OR, right.value));
			}
		} else {
			// 上記いずれのケースにも該当しないならば論理演算（演算子と右辺）は
			// 存在しないということだから引数として渡された式（左辺になるはずだった）をそのまま返す
			return Result.success(left);
		}
	}
	
	/**
	 * 引用符なしで記述された文字列（プロパティもしくは値）をパースして返す.
	 * 使用可能な文字は、英数字、アンダスコア、そしてハイフンのみ。
	 * @return パースした文字列
	 * @throws ParseError 構文エラーが見つかった場合
	 */
	private Result<String> parseNonQuotedString(final Reader in) {
		// パースした文字列を格納するバッファを初期化
		final StringBuilder sb = new StringBuilder();
		// 文字列の終端に達するまで繰り返し処理
		while (!in.hasReachedEof()) {
			// 現在文字をチェック
			final char c = in.current();
			if (('0' <= c && c <= '9') || 
					('A'  <= c && c <= 'Z') ||
					('a' <= c && c <= 'z') ||
					(c == '_' || c == '-')) {
				// 許された文字列であればバッファに追加して次に文字に進む
				sb.append(c);
				in.next();
			} else {
				// 許された文字以外が登場したらそこで処理を終える
				break;
			}
		}
		if (sb.length() > 0) {
			return Result.success(sb.toString());
		} else {
			return Result.failure("Value is not found.");
		}
	}
	
	/**
	 * 値（比較演算の右辺）をパースして返す.
	 * @return パースした値
	 * @throws ParseError 構文エラーが見つかった場合
	 */
	private Result<String> parseValue(final Reader in) {
		// 現在文字をチェック
		final char c = in.current();
		if (c == '"' || c == '\'') {
			// 現在文字が引用符である場合は、引用符で囲われた文字列としてパース
			return parsers.parseQuotedString(in);
		} else {
			// そうでない場合は、英数字とハイフンとアンダースコアのみからなる文字列としてパース
			return parseNonQuotedString(in);
		}
	}
	
	/**
	 * 演算子をパースして返す.
	 * @return パースした演算子
	 * @throws ParseError 構文エラーが見つかった場合
	 */
	private Result<Operator> parseComparativeOperator(final Reader in) {
		final char c0 = in.current();
		
		if (c0 == '=') {
			if (in.next() != '=') return Result.failure('=', in.current());
			in.next();
			return Result.success(Operator.EQUALS);
			
		} else if (c0 == '!') {
			if (in.next() != '=') return Result.failure('=', in.current());
			in.next();
			return Result.success(Operator.NOT_EQUALS);
			
		} else if (c0 == '^') {
			if (in.next() != '=') return Result.failure('=', in.current());
			in.next();
			return Result.success(Operator.STARTS_WITH);
			
		} else if (c0 == '$') {
			if (in.next() != '=') return Result.failure('=', in.current());
			in.next();
			return Result.success(Operator.ENDS_WITH);
			
		} else if (c0 == '*') {
			if (in.next() != '=') return Result.failure('=', in.current());
			in.next();
			return Result.success(Operator.CONTAINS);
			
		} else if (c0 == 'i') {
			if (in.next() != 's') return Result.failure('s', in.current());
			in.next();
			parsers.skipWhitespace(in);
			if (in.current() != 'n') return Result.failure('n', in.current());
			
			final char c1 = in.next();
			if (c1 == 'o') {
				if (in.next() != 't') return Result.failure('t', in.current());
				in.next();
				parsers.skipWhitespace(in);
				if (in.current() != 'n') return Result.failure('n', in.current());
				if (in.next() != 'u') return Result.failure('u', in.current());
				if (in.next() != 'l') return Result.failure('l', in.current());
				if (in.next() != 'l') return Result.failure('l', in.current());
				in.next();
				return Result.success(Operator.IS_NOT_NULL);
			} else if (c1 == 'u') {
				if (in.next() != 'l') return Result.failure('l', in.current());
				if (in.next() != 'l') return Result.failure('l', in.current());
				in.next();
				return Result.success(Operator.IS_NULL);
			}
			
		}
		return Result.failure("Invalid syntax found on comparative expression.");
	}
	
}
