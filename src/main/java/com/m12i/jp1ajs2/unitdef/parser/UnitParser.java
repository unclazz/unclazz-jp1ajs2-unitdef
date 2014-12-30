package com.m12i.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.ParamValue;
import com.m12i.jp1ajs2.unitdef.ParamValueFormat;
import com.m12i.jp1ajs2.unitdef.Tuple;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.parser.Parsers.Options;

public final class UnitParser extends AbstractParser<Unit> {
	private static final Options OPTIONS = new Options();
	static {
		OPTIONS.setEscapePrefixInDoubleQuotes('#');
	}
	
	public UnitParser() {
		super(OPTIONS);
	}
	
	public Unit parse(final Input in) {
		parsers.skipWhitespace(in);
		final Unit def = parseUnit(in, null);
		parsers.skipWhitespace(in);
		if (in.reachedEof()) {
			return def;
		} else {
			throw ParseException.syntaxError(in);
		}
	}
	
	Unit parseUnit(final Input in, final String context) {
		try {
			// ユニット定義の開始キーワードを読み取る
			parsers.skipWhitespace(in);
			parsers.skipWord(in, "unit");
	
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
			parsers.check(in, ';');
	
			in.next();
			parsers.skipWhitespace(in);
	
			// ユニット定義パラメータの開始カッコを読み取る
			parsers.check(in, '{');
			in.next();
			
			// 空白をスキップ
			parsers.skipWhitespace(in);
	
			// '}'が登場したらそこでユニット定義は終わり
			if (in.current() == '}') {
				in.next();
				return new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
						fullQualifiedName,
						params,
						subUnits);
			}
	
			// "unit"で始まらないならそれはパラメータ
			if(! in.restStartsWith("unit")){
				while (in.unlessEof()) {
					// パラメータを読み取る
					params.add(parseParam(in));
					// パラメータ読み取り後にもかかわらず現在文字が';'でないなら構文エラー
					parsers.check(in, ';');
					in.next();
					parsers.skipWhitespace(in);
					
					// '}'が登場したらそこでユニット定義は終わり
					if (in.current() == '}') {
						in.next();
						return new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
								fullQualifiedName,
								params,
								subUnits);
						
					/// "unit"と続くならパラメータの定義は終わりサブユニットの定義に移る
					}else if(in.restStartsWith("unit")){
						break;
					}
				}
			}
			
			// "unit"で始まるならそれはサブユニット
			while (in.restStartsWith("unit")) {
				subUnits.add(parseUnit(in, fullQualifiedName));
				parsers.skipWhitespace(in);
			}
			
			parsers.check(in, '}');
			in.next();
			final UnitImpl unit = new UnitImpl(attrs[0], attrs[1], attrs[2], attrs[3],
					fullQualifiedName,
					params,
					subUnits);
			
			return unit;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}

	Param parseParam(final Input in) {
		try {
			// '='より以前のパラメータ名の部分を取得する
			final String name = parsers.parseUntil(in, '=');
			// パラメータ名が存在しない場合は構文エラー
			if (name.length() == 0) {
				throw ParseException.syntaxError(in);
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
					throw ParseException.syntaxError(in);
				}
			}
			// 読取った結果を使ってパラメータを初期化して返す
			return new ParamImpl(name, values);
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	ParamValue parseParamValue(final Input in) {
		switch (in.current()) {
		case '(':
			final Tuple t = parseTuple(in);
			return new ParamValue() {
				@Override
				public Tuple getTupleValue() {
					return t;
				}
				@Override
				public String getStringValue() {
					return t.toString();
				}
				@Override
				public String toString() {
					return t.toString();
				}
				@Override
				public ParamValueFormat getFormat() {
					return ParamValueFormat.TUPLE;
				}
			};
		case '"':
			final String q = parsers.parseQuotedString(in);
			return new ParamValue() {
				@Override
				public Tuple getTupleValue() {
					return null;
				}
				@Override
				public String getStringValue() {
					return q;
				}
				@Override
				public String toString() {
					return "\"" + q.replaceAll("(#|\")", "#$1") + "\"";
				}
				@Override
				public ParamValueFormat getFormat() {
					return ParamValueFormat.QUOTED_STRING;
				}
			};
		default:
			final String s = parseRawString(in);
			return new ParamValue() {
				@Override
				public Tuple getTupleValue() {
					return null;
				}
				@Override
				public String getStringValue() {
					return s;
				}
				@Override
				public String toString() {
					return s;
				}
				@Override
				public ParamValueFormat getFormat() {
					return ParamValueFormat.RAW_STRING;
				}
			};
		}
	}

	String parseRawString(final Input in) {
		try {
			final StringBuilder sb = new StringBuilder();
			while (in.unlessEof()) {
				final char c = in.current();
				if (c == ',' || c == ';') {
					break;
				} else if (c == '"') {
					final String quoted = parsers.parseQuotedString(in);
					sb.append('"').append(quoted.replaceAll("#", "##").replaceAll("\"", "#\"")).append('"');
				} else {
					sb.append(c);
					in.next();
				}
			}
			return sb.toString();
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	Tuple parseTuple(final Input in) {
		try {
			parsers.check(in, '(');
			final List<Tuple.Entry> values = new ArrayList<Tuple.Entry>();
			in.next();
			while (in.unlessEof() && in.current() != ')') {
				final StringBuilder sb0 = new StringBuilder();
				final StringBuilder sb1 = new StringBuilder();
				boolean hasKey = false;
				while (in.unlessEof() && (in.current() != ')' && in.current() != ',')) {
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
			parsers.check(in, ')');
			in.next();
			return values.size() == 0 ? Tuple.EMPTY_TUPLE : new TupleImpl(values);
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
	
	String parseAttr(final Input in) {
		try {
			final StringBuilder sb = new StringBuilder();
			while(in.unlessEof()) {
				final char c = in.current();
				if(c == ',' || c == ';') {
					return sb.length() == 0 ? null : sb.toString();
				}
				sb.append(c);
				in.next();
			}
			throw ParseException.syntaxError(in);
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
}
