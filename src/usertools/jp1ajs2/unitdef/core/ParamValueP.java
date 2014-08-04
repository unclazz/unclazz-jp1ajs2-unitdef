package usertools.jp1ajs2.unitdef.core;

import com.m12i.code.parse.ParseException;

import unklazz.parsec.Parser;
import unklazz.parsec.Reader;
import unklazz.parsec.Result;
import static usertools.jp1ajs2.unitdef.core.UnitP.*;

final class ParamValueP implements Parser<ParamValue> {
	static final TupleP tupleP = new TupleP();
	
	private final StringBuilder buff = new StringBuilder();
	
	@Override
	public final Result<ParamValue> parse(Reader in) {
		final char c = in.current();
		
		if (c == '(') {
			// タプルとしてみなす
			// ＊自由入力欄の内容は事実上もれなく引用符で囲われるらしく、結果としてこのような簡単な判定でもOK
			final Result<Tuple> tupleR = tupleP.parse(in);
			// チェック
			if (tupleR.isFailure()) error("タプルのパースに失敗.", in);
			// 読み取り結果を返す
			final ParamValue v = new TupleParamValue(tupleR.get());
			return Result.success(v);
		} else if (c == '"') {
			// 引用符で囲われた文字列としてみなす
			final Result<String> quotedR = quotedP.parse(in);
			// チェック
			if (quotedR.isFailure()) error("引用符で囲われた文字列のパースに失敗.", in);
			// 読み取り結果を返す
			final ParamValue v = new QuotedParamValue(quotedR.get());
			return Result.success(v);
		} else {
			// それ以外の場合ともかく文字列としてみなす
			final String raw = parseRawString(in);
			final ParamValue v = new RawStringParamValue(raw);
			return Result.success(v);
		}
	}

	private String parseRawString(final Reader in) throws ParseException {
		buff.setLength(0);
		while (!in.hasReachedEof()) {
			final char c = in.current();
			if (c == ',' || c == ';') {
				break;
			} else if (c == '"') {
				final String quoted = UnitP.quotedP.parse(in).get();
				buff.append('"').append(quoted.replaceAll("#", "##").replaceAll("\"", "#\"")).append('"');
			} else {
				buff.append(c);
				in.next();
			}
		}
		return buff.toString();
	}
	
	@Override
	public final String tokenName() {
		return "jp1/ajs2-unit-definition-parameter-value";
	}

}
