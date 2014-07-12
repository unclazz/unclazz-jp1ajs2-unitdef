package usertools.jp1ajs2.unitdef.core;

import java.util.ArrayList;
import java.util.List;

import usertools.jp1ajs2.unitdef.core.TupleEntryImpl;

import com.m12i.code.parse.Parsable;
import com.m12i.code.parse.ParseException;
import com.m12i.code.parse.ParserTemplate;

public class Parser extends ParserTemplate<Unit> {
	@Override
	protected
	void code(Parsable p) {
		super.code(p);
	}
	
	@Override
	protected Unit parseMain() throws ParseException {
		skipSpace();
		final Unit def = parseUnit(null);
		skipSpace();
		if (hasReachedEof()) {
			return def;
		} else {
			throw ParseException.syntaxError(code());
		}
	}

	@Override
	public String blockCommentEnd() {
		return "*/";
	}

	@Override
	public String blockCommentStart() {
		return "/*";
	}

	@Override
	public char escapePrefixInDoubleQuotes() {
		return '#';
	}

	@Override
	public char escapePrefixInSingleQuotes() {
		return '\u0000';
	}

	@Override
	public String lineCommentStart() {
		return null;
	}

	@Override
	public boolean skipCommentWithSpace() {
		return true;
	}

	public Unit parseUnit(String context) throws ParseException {
		// ユニット定義の開始キーワードを読み取る
		skipSpace();
		skipWord("unit");

		// ユニット定義属性その他の初期値を作成
		final String[] attrs = new String[] { null, null, null, null };
		final List<Param> params = new ArrayList<Param>();
		final List<Unit> subUnits = new ArrayList<Unit>();

		// ユニット定義属性を読み取る
		// 属性は最大で4つ、カンマ区切りで指定される
		for (int i = 0; i < 4
				&& (currentIsAnyOf('=', ',')); i++) {
			next();
			attrs[i] = parseAttr();
		}
		
		final String fullQualifiedName = (context == null ? "" : context) + "/" + attrs[0];

		// 属性の定義は「；」で終わる
		currentMustBe(';');

		next();
		skipSpace();

		// ユニット定義パラメータの開始カッコを読み取る
		currentMustBe('{');
		next();
		
		// 空白をスキップ
		skipSpace();

		// '}'が登場したらそこでユニット定義は終わり
		if (currentIs('}')) {
			next();
			return new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
					fullQualifiedName,
					params,
					subUnits);
		}

		// "unit"で始まらないならそれはパラメータ
		if(! remainingCodeStartsWith("unit")){
			while (! hasReachedEof()) {
				// パラメータを読み取る
				params.add(parseParam());
				// パラメータ読み取り後にもかかわらず現在文字が';'でないなら構文エラー
				currentMustBe(';');
				next();
				skipSpace();
				
				// '}'が登場したらそこでユニット定義は終わり
				if (currentIs('}')) {
					next();
					return new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
							fullQualifiedName,
							params,
							subUnits);
					
				/// "unit"と続くならパラメータの定義は終わりサブユニットの定義に移る
				}else if(remainingCodeStartsWith("unit")){
					break;
				}
			}
		}
		
		// "unit"で始まるならそれはサブユニット
		while (remainingCodeStartsWith("unit")) {
			subUnits.add(parseUnit(fullQualifiedName));
			skipSpace();
		}
		
		currentMustBe('}');
		next();
		final UnitImpl unit = new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
				fullQualifiedName,
				params,
				subUnits);
		
		return unit;
	}

	public Param parseParam() throws ParseException {
		// '='より以前のパラメータ名の部分を取得する
		final String name = parseUntil('=');
		// パラメータ名が存在しない場合は構文エラー
		if (name.length() == 0) {
			throw ParseException.syntaxError(code());
		}
		// パラメータ値を一時的に格納するリストを初期化
		final List<ParamValue> values = new ArrayList<ParamValue>();
		// パラメータの終端文字';'が登場するまで繰り返し
		while (currentIsNot(';')) {
			// '='や','を読み飛ばして前進
			next();
			// パラメータ値を読み取っていったんリストに格納
			values.add(parseParamValue());
			// パラメータ値読取り後にもかかわらず現在文字が区切り文字以外であれば構文エラー
			if (currentIsNotAnyOf(',', ';')) {
				throw ParseException.syntaxError(code());
			}
		}
		// 読取った結果を使ってパラメータを初期化して返す
		return new ParamImpl(name, values);
	}
	
	public ParamValue parseParamValue()
			throws ParseException {
		switch (current()) {
		case '(':
			final Tuple t = parseTuple();
			return new ParamValue() {
				@Override
				public Tuple getTuploidValue() {
					return t;
				}
				@Override
				public String getUnclassifiedValue() {
					return t.toString();
				}
				@Override
				public boolean is(ParamValueType type) {
					return ParamValueType.TUPLOID == type;
				}
				@Override
				public String getStringValue() {
					return t.toString();
				}
				@Override
				public String toString() {
					return t.toString();
				}
			};
		case '"':
			final String q = parseQuotedString();
			return new ParamValue() {
				@Override
				public String getUnclassifiedValue() {
					return q;
				}
				@Override
				public Tuple getTuploidValue() {
					return null;
				}
				@Override
				public boolean is(ParamValueType type) {
					return ParamValueType.STRING == type;
				}
				@Override
				public String getStringValue() {
					return q;
				}
				@Override
				public String toString() {
					return "\"" + q.replaceAll("(#|\")", "#$1") + "\"";
				}
			};
		default:
			final String s = parseUntil(',', ';');
			return new ParamValue() {
				@Override
				public String getUnclassifiedValue() {
					return s;
				}
				@Override
				public Tuple getTuploidValue() {
					return null;
				}
				@Override
				public boolean is(ParamValueType type) {
					return ParamValueType.UNCLASSIFIED == type;
				}
				@Override
				public String getStringValue() {
					return s;
				}
				@Override
				public String toString() {
					return s;
				}
			};
		}
	}

	public Tuple parseTuple() throws ParseException {
		currentMustBe('(');
		final List<TupleEntry> values = new ArrayList<TupleEntry>();
		next();
		while (!hasReachedEof() && currentIsNot(')')) {
			final StringBuilder sb0 = new StringBuilder();
			final StringBuilder sb1 = new StringBuilder();
			boolean hasKey = false;
			while (!hasReachedEof() && currentIsNotAnyOf(')', ',')) {
				if (currentIs('=')) {
					hasKey = true;
					next();
				}
				(hasKey ? sb1 : sb0).append(current());
				next();
			}
			values.add(hasKey ? new TupleEntryImpl(sb0.toString(), sb1.toString())
					: new TupleEntryImpl(sb0.toString()));
			if (currentIs(')')) {
				break;
			}
			next();
		}
		currentMustBe(')');
		next();
		return values.size() == 0 ? Tuple.EMPTY_TUPLE : new TupleImpl(values);
	}
	
	public String parseAttr() throws ParseException {
		final StringBuilder sb = new StringBuilder();
		while(! hasReachedEof()) {
			if(currentIsAnyOf(',', ';')) {
				return sb.length() == 0 ? null : sb.toString();
			}
			sb.append(current());
			next();
		}
		throw ParseException.syntaxError(code());
	}

}
