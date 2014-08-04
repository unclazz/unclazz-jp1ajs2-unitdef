package usertools.jp1ajs2.unitdef.core;

import java.util.ArrayList;
import java.util.List;

import unklazz.parsec.ParseError;
import unklazz.parsec.Parser;
import unklazz.parsec.Reader;
import unklazz.parsec.Result;
import unklazz.parsec.parsers.Comment;
import unklazz.parsec.parsers.QuotedString;
import unklazz.parsec.parsers.RawString;
import unklazz.parsec.parsers.Separator;
import unklazz.parsec.parsers.Sequence;
import unklazz.parsec.parsers.WhiteSpaces;
import unklazz.parsec.parsers.Word;

final class UnitP implements Parser<Unit> {
	
	static final Comment commentP = Comment.comment("/*", "*/");
	static final WhiteSpaces spacesP = WhiteSpaces.withComment(commentP);
	static final QuotedString quotedP = QuotedString.doubleQuotedString('#');
	static final Word unitEqualP = Word.word("unit=");
	static final RawString attrP = RawString.rawString(',', ';');
	static final Separator semicolonP = Separator.semicolon();
	static final Sequence<String> attrsP = Sequence.sequence(attrP, Separator.comma());
	static final ParamP paramP = new ParamP();

	final UnitStack unitStack = new UnitStack();
	
	static void error(String msg, Reader in) {
		throw new ParseError(msg, in);
	}
	
	private static boolean subunitStartHere(Reader in) {
		return in.line().substring(in.columnNo() - 1).startsWith("unit=");
	}
	
	@Override
	public Result<Unit> parse(Reader in) {
		// ユニット定義の開始キーワードを読み取る
		spacesP.parse(in);
		unitEqualP.parse(in);
		
		// ユニット定義属性を読み取る
		// 属性は最大で4つ、カンマ区切りで指定される
		final List<String> attrsR = attrsP.parse(in).get();
		final int attrsSize = attrsR.size();
		final String unitName = attrsR.get(0);
		final String permMode = (attrsSize > 1 && !attrsR.get(1).isEmpty()) ? attrsR.get(1) : null;
		final String ownName = (attrsSize > 2 && !attrsR.get(2).isEmpty()) ? attrsR.get(2) : null;
		final String resrcGroup = (attrsSize > 3 && !attrsR.get(3).isEmpty()) ? attrsR.get(3) : null;
		unitStack.push(unitName);
		
		// チェック
		if (in.current() != ';') error("ユニット定義属性の末尾には'}'が必要.", in);
		
		in.next();
		spacesP.parse(in);
		
		// チェック
		if (in.current() != '{') error("ユニット定義パラメータのまえには'{'が必要.", in);
		
		in.next();
		spacesP.parse(in);
		
		// ユニット定義属性その他の初期値を作成
		final List<Param> params = new ArrayList<Param>();
		final List<Unit> subUnits = new ArrayList<Unit>();
		
		// '}'が登場したらそこでユニット定義は終わり
		// ＊リファレンスによれば必須のユニット定義パラメータもあるのでこの位置での処理完了はありえないはず
		if (in.current() == '}') {
			// 現在位置を前進させる
			in.next();
			// ユニットオブジェクトを初期化
			final Unit u = new UnitImpl(unitName, permMode, ownName, resrcGroup, unitStack.fqn(), params, subUnits);
			// スタックからこのユニットの名前を取り去る
			unitStack.pop();
			// 呼び出し元に結果を返す
			return Result.success(u);
		}
		
		// "unit"で始まらないならそれはパラメータ
		if (!subunitStartHere(in)) {
			while (! in.hasReachedEof()) {
				final Result<Param> paramR = paramP.parse(in);
				// チェック
				if (paramR.isFailure()) error("'{'のあとにはユニット定義パラメータが必要.", in);
				// チェック
				if (in.current() != ';') error("ユニット定義パラメータの末尾には';'が必要.", in);
				// パラメータをリストに格納
				params.add(paramR.get());
				// 現在位置を前進させる
				in.next();
				// 空白文字をスキップ
				spacesP.parse(in);
				// '}'が登場したらそこでユニット定義は終わり
				if (in.current() == '}') {
					// 現在位置を前進させる
					in.next();
					// ユニットオブジェクトを初期化
					final Unit u = new UnitImpl(unitName, permMode, ownName, resrcGroup, unitStack.fqn(), params, subUnits);
					// スタックからこのユニットの名前を取り去る
					unitStack.pop();
					// 呼び出し元に結果を返す
					return Result.success(u);
				}
				
				// "unit="と続くならパラメータの定義は終わりサブユニットの定義に移る
				if (subunitStartHere(in)) {
					break;
				}
				
			}
		}
		
		// "unit"で始まるならそれはサブユニット
		while (subunitStartHere(in)) {
			subUnits.add(this.parse(in).get());
			spacesP.parse(in);
		}

		// チェック
		if (in.current() != '}') error("ユニット定義パラメータの末尾には'{'が必要.", in);
		// 現在位置を前進させる
		in.next();
		// ユニットオブジェクトを初期化
		final Unit u = new UnitImpl(unitName, permMode, ownName, resrcGroup, unitStack.fqn(), params, subUnits);
		// スタックからこのユニットの名前を取り去る
		unitStack.pop();
		// 呼び出し元に結果を返す
		return Result.success(u);
	}

	@Override
	public String tokenName() {
		return "jp1/ajs2-unit-definition";
	}

}
