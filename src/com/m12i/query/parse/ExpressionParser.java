package com.m12i.query.parse;

import com.m12i.code.parse.ParseException;
import com.m12i.code.parse.ParserTemplate;

class ExpressionParser extends ParserTemplate<Expression>{

	@Override
	protected Expression parseMain() throws ParseException {
		// 何はともあれ空白文字をスキップ
		skipSpace();
		if (hasReachedEof()) {
			// 空文字列もしくは空白文字のみからなるクエリの場合はエラー
			throw new ParseException("Empty string.", code());
		}
		// それ以外の場合は再帰的にクエリを構文解析
		return parseExpression(true);
	}
	
	/**
	 * 式（論理演算・比較演算）をパースして返す.
	 * 再帰下降オプションは式のパースが「左結合」で実行されるようにするためのパラメータ。
	 * @param recursive 再帰下降オプション
	 * @return パースした式
	 * @throws ParseException 構文エラーが見つかった場合
	 */
	private Expression parseExpression(boolean recursive) throws ParseException {
		// 再帰呼び出し時を想定してとにかく空白文字をスキップ
		skipSpace();
		// 現在文字をチェック
		if (currentIs('!')) {
			// 現在文字が"!"であれば単行論理演算子とみなして処理を進める
			next();
			skipSpace();
			// 単行論理演算子と右辺の式（これから再帰的にパースされる）をもとに論理演算オブジェクトを生成して返す
			return Expression.logical(Operator.NOT, parseExpression(true));
		} else if(currentIs('(')) {
			// 現在文字が"("であれば結合方法を制御する丸括弧の開始とみなして処理を進める
			next();
			// 括弧の内側にあるのはともかく何かしらの式だからそれをパースする
			final Expression e = parseExpression(true);
			// 式の後の空白文字をスキップ
			skipSpace();
			// 式の後には")"がつづく
			currentMustBe(')');
			next();
			// 再帰下降オプションをチェック
			if (!recursive) {
				// 再帰下降を許されていない場合は先ほど読み取った式を返す
				return e;
			} else {
				// 再帰下降を許されている場合は後続の論理演算（もしあれば）も含めて処理した結果を返す
				return parseLogical(e);
			}
		} else {
			// 現在文字が単行論理演算子でも丸括弧でもなかった場合は比較演算とみなして処理を進める
			// まず現在文字をチェックしてプロパティ記述をパースする
			final String prop = currentIsAnyOf('"', '\'') ? parseQuotedString() : parseNonQuotedString();
			// 空白文字をスキップ
			skipSpace();
			// 次に演算子をパースする
			final Operator op = parseComparativeOperator();
			// 空白文字をスキップ
			skipSpace();
			// 演算子の種類をチェックして値の記述をパースする
			final String value = (op == Operator.IS_NOT_NULL || op == Operator.IS_NULL) ? null : parseValue();
			// プロパティ、演算子、値の3値から比較演算オブジェクトを構成する
			final Expression expr0 = Expression.comparative(Expression.property(prop), op, Expression.value(value));
			// 再帰下降オプションをチェック
			if (!recursive) {
				// 再帰下降を許されていない場合は先ほど読み取った式を返す
				return expr0;
			} else {
				// 再帰下降を許されている場合は後続の論理演算（もしあれば）も含めて処理した結果を返す
				return parseLogical(expr0);
			}
			
		}
	}
	
	/**
	 * 論理演算式をパースして返す.
	 * 引数として左辺にあたる式を受け取り、後続のクエリ文字列から論理演算子と右辺の式を読み取って、その3値から論理演算子式を構成して返す。
	 * 後続のクエリ文字列に論理演算子が見つからなかった場合は、引数として与えられた式をそのまま返す。
	 * @param left 論理演算の左辺
	 * @return パースした式
	 * @throws ParseException 構文エラーが見つかった場合
	 */
	private Expression parseLogical(Expression left) throws ParseException {
		// まずは空白文字をスキップ
		skipSpace();
		// 現在文字をチェックして処理分岐
		if (currentIs('a')) {
			// 現在文字が"a"ならば"nd"が続かない限り構文エラー
			nextMustBe('n');
			nextMustBe('d');
			next();
			skipSpace();
			// 引数として渡された式（左辺）と最前パースした演算子、
			// そしてこれからパースする式（右辺）で論理演算オブジェクトを構成して返す
			// ＊ただし右辺の式をパースするにあたり「右結合」にならないよう再帰下降オプションはOFFにする
			return parseLogical(Expression.logical(left, Operator.AND, parseExpression(false)));
		} else if (currentIs('&')) {
			// 現在文字が"&"ならば"&"が続かない限り構文エラー
			nextMustBe('&');
			next();
			skipSpace();
			// "and"の場合と同様に処理
			return parseLogical(Expression.logical(left, Operator.AND, parseExpression(false)));
		} else if (currentIs('o')) {
			// 現在文字が"o"ならば"r"が続かない限り構文エラー
			nextMustBe('r');
			next();
			skipSpace();
			// "and"の場合と同様に処理
			return parseLogical(Expression.logical(left, Operator.OR, parseExpression(false)));
		} else if (currentIs('|')) {
			// 現在文字が"|"ならば"|"が続かない限り構文エラー
			nextMustBe('|');
			next();
			skipSpace();
			// "and"の場合と同様に処理
			return parseLogical(Expression.logical(left, Operator.OR, parseExpression(false)));
		} else {
			// 上記いずれのケースにも該当しないならば論理演算（演算子と右辺）は
			// 存在しないということだから引数として渡された式（左辺になるはずだった）をそのまま返す
			return left;
		}
	}
	
	/**
	 * 引用符なしで記述された文字列（プロパティもしくは値）をパースして返す.
	 * 使用可能な文字は、英数字、アンダーバー、そしてハイフンのみ。
	 * @return パースした文字列
	 * @throws ParseException 構文エラーが見つかった場合
	 */
	private String parseNonQuotedString() throws ParseException {
		final StringBuilder sb = new StringBuilder();
		while (!hasReachedEof()) {
			if (currentIsBetween('0', '9') || 
					currentIsBetween('A', 'Z') ||
					currentIsBetween('a', 'z') ||
					currentIsAnyOf('_', '-')) {
				sb.append(current());
				next();
			} else {
				break;
			}
		}
		return sb.toString();
	}
	
	private String parseValue() throws ParseException {
		if (currentIsAnyOf('"', '\'')) {
			return parseQuotedString();
		} else {
			final String value = parseNonQuotedString();
			return value.trim();
		}
	}
	
	/**
	 * 演算子をパースして返す.
	 * @return パースした演算子
	 * @throws ParseException 構文エラーが見つかった場合
	 */
	private Operator parseComparativeOperator() throws ParseException {
		if (currentIs('=')) {
			nextMustBe('=');
			next();
			return Operator.EQUALS;
		} else if (currentIs('!')) {
			nextMustBe('=');
			next();
			return Operator.NOT_EQUALS;
		} else if (currentIs('^')) {
			nextMustBe('=');
			next();
			return Operator.STARTS_WITH;
		} else if (currentIs('$')) {
			nextMustBe('=');
			next();
			return Operator.ENDS_WITH;
		} else if (currentIs('*')) {
			nextMustBe('=');
			next();
			return Operator.CONTAINS;
		} else if (currentIs('i')) {
			next();
			currentMustBe('s');
			next();
			skipSpace();
			currentMustBe('n');
			next();
			if (currentIs('o')) {
				next();
				currentMustBe('t');
				next();
				skipSpace();
				currentMustBe('n');
				next();
				currentMustBe('u');
				next();
				currentMustBe('l');
				next();
				currentMustBe('l');
				next();
				return Operator.IS_NOT_NULL;
			} else if (currentIs('u')) {
				next();
				currentMustBe('l');
				next();
				currentMustBe('l');
				next();
				return Operator.IS_NULL;
			}
			
		}
		throw new ParseException("Invalid syntax found on comparative expression.", code());
	}
	
	@Override
	public char escapePrefixInSingleQuotes() {
		return '\\';
	}

	@Override
	public char escapePrefixInDoubleQuotes() {
		return '\\';
	}

	@Override
	public String lineCommentStart() {
		return null;
	}

	@Override
	public String blockCommentStart() {
		return null;
	}

	@Override
	public String blockCommentEnd() {
		return null;
	}

	@Override
	public boolean skipCommentWithSpace() {
		return false;
	}

}
