package com.m12i.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.ParamValue;
import com.m12i.jp1ajs2.unitdef.Tuple;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.parser.Parsers.Options;

public class UnitParser {
	private static String rest(final Input in) {
		return in.hasReachedEof() ? "" : in.line().substring(in.columnNo() - 1);
	}
	
	private final Parsers coreParsers;
	
	public UnitParser() {
		final Options options = new Options();
		options.setEscapePrefixInDoubleQuotes('#');
		coreParsers = new Parsers(options);
	}
	
	public Unit parse(final Input in) {
		coreParsers.skipWhitespace(in);
		final Unit def = parseUnit(in, null);
		coreParsers.skipWhitespace(in);
		if (in.hasReachedEof()) {
			return def;
		} else {
			ParseError.syntaxError(in);
			return null;
		}
	}
	
	public Unit parseUnit(final Input in, final String context) {
		// ユニット定義の開始キーワードを読み取る
		coreParsers.skipWhitespace(in);
		if(coreParsers.skipWord(in, "unit").failed)
			ParseError.syntaxError(in);

		// ユニット定義属性その他の初期値を作成
		final String[] attrs = new String[] { null, null, null, null };
		final List<Param> params = new ArrayList<Param>();
		final List<Unit> subUnits = new ArrayList<Unit>();

		// ユニット定義属性を読み取る
		// 属性は最大で4つ、カンマ区切りで指定される
		final char c = in.current();
		for (int i = 0; i < 4 && (c == '=' || c == ','); i++) {
			in.next();
			attrs[i] = parseAttr(in);
		}
		
		final String fullQualifiedName = (context == null ? "" : context) + "/" + attrs[0];

		// 属性の定義は「；」で終わる
		coreParsers.check(in, ';');

		in.next();
		coreParsers.skipWhitespace(in);

		// ユニット定義パラメータの開始カッコを読み取る
		coreParsers.check(in, '{');
		in.next();
		
		// 空白をスキップ
		coreParsers.skipWhitespace(in);

		// '}'が登場したらそこでユニット定義は終わり
		if (in.current() == '}') {
			in.next();
			return new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
					fullQualifiedName,
					params,
					subUnits);
		}

		// "unit"で始まらないならそれはパラメータ
		if(! rest(in).startsWith("unit")){
			while (! in.hasReachedEof()) {
				// パラメータを読み取る
				params.add(parseParam(in));
				// パラメータ読み取り後にもかかわらず現在文字が';'でないなら構文エラー
				coreParsers.check(in, ';');
				in.next();
				coreParsers.skipWhitespace(in);
				
				// '}'が登場したらそこでユニット定義は終わり
				if (in.current() == '}') {
					in.next();
					return new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
							fullQualifiedName,
							params,
							subUnits);
					
				/// "unit"と続くならパラメータの定義は終わりサブユニットの定義に移る
				}else if(rest(in).startsWith("unit")){
					break;
				}
			}
		}
		
		// "unit"で始まるならそれはサブユニット
		while (rest(in).startsWith("unit")) {
			subUnits.add(parseUnit(in, fullQualifiedName));
			coreParsers.skipWhitespace(in);
		}
		
		coreParsers.check(in, '}');
		in.next();
		final UnitImpl unit = new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
				fullQualifiedName,
				params,
				subUnits);
		
		return unit;
	}

	public Param parseParam(final Input in) {
		// '='より以前のパラメータ名の部分を取得する
		final String name = coreParsers.parseUntil(in, '=').value;
		// パラメータ名が存在しない場合は構文エラー
		if (name.length() == 0) {
			ParseError.syntaxError(in);
		}
		// パラメータ値を一時的に格納するリストを初期化
		final List<ParamValue> values = new ArrayList<ParamValue>();
		// パラメータの終端文字';'が登場するまで繰り返し
		while (in.current() != ';') {
			// '='や','を読み飛ばして前進
			in.next();
			// パラメータ値を読み取っていったんリストに格納
			values.add(parseParamValue(in));
			// パラメータ値読取り後にもかかわらず現在文字が区切り文字以外であれば構文エラー
			if (in.current() != ',' && in.current() != ';') {
				ParseError.syntaxError(in);
			}
		}
		// 読取った結果を使ってパラメータを初期化して返す
		return new ParamImpl(name, values);
	}
	
	public ParamValue parseParamValue(final Input in) {
		switch (in.current()) {
		case '(':
			final Tuple t = parseTuple(in);
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
			final String q = coreParsers.parseQuotedString(in).value;
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
			final String s = parseRawString(in);
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

	private String parseRawString(final Input in) {
		final StringBuilder sb = new StringBuilder();
		while (!in.hasReachedEof()) {
			final char c = in.current();
			if (c == ',' || c == ';') {
				break;
			} else if (c == '"') {
				final String quoted = coreParsers.parseQuotedString(in).value;
				sb.append('"').append(quoted.replaceAll("#", "##").replaceAll("\"", "#\"")).append('"');
			} else {
				sb.append(c);
				in.next();
			}
		}
		return sb.toString();
	}
	
	public Tuple parseTuple(final Input in) {
		coreParsers.check(in, '(');
		final List<Tuple.Entry> values = new ArrayList<Tuple.Entry>();
		in.next();
		while (!in.hasReachedEof() && in.current() != ')') {
			final StringBuilder sb0 = new StringBuilder();
			final StringBuilder sb1 = new StringBuilder();
			boolean hasKey = false;
			while (!in.hasReachedEof() && (in.current() != ')' && in.current() != ',')) {
				if (in.current() == '=') {
					hasKey = true;
					in.next();
				}
				(hasKey ? sb1 : sb0).append(in.current());
				in.next();
			}
			values.add(hasKey ? new TupleImpl.EntryImpl(sb0.toString(), sb1.toString())
					: new TupleImpl.EntryImpl(sb0.toString()));
			if (in.current() == ')') {
				break;
			}
			in.next();
		}
		coreParsers.check(in, ')');
		in.next();
		return values.size() == 0 ? Tuple.EMPTY_TUPLE : new TupleImpl(values);
	}
	
	public String parseAttr(final Input in) {
		final StringBuilder sb = new StringBuilder();
		while(! in.hasReachedEof()) {
			final char c = in.current();
			if(c == ',' || c == ';') {
				return sb.length() == 0 ? null : sb.toString();
			}
			sb.append(c);
			in.next();
		}
		ParseError.syntaxError(in);
		return null;
	}

}
