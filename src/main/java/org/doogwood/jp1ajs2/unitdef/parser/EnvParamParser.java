package org.doogwood.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.List;

import org.doogwood.jp1ajs2.unitdef.EnvironmentVariable;
import org.doogwood.jp1ajs2.unitdef.util.ListUtils;
import org.doogwood.parse.AbstractParser;
import org.doogwood.parse.Input;
import org.doogwood.parse.InputExeption;
import org.doogwood.parse.ParseException;
import org.doogwood.parse.ParseOptions;
import org.doogwood.parse.ParseResult;

public final class EnvParamParser extends AbstractParser<List<EnvironmentVariable>> {

	private static final ParseOptions OPTIONS = new ParseOptions();
	static {
		OPTIONS.setEscapePrefixInDoubleQuotes('#');
	}
	
	public EnvParamParser() {
		super(OPTIONS);
	}
	
	public ParseResult<List<EnvironmentVariable>> parse(final Input in) {
		try {
			final List<EnvironmentVariable> varList = new ArrayList<EnvironmentVariable>();
			// 空白をトリム
			helper.skipWhitespace(in);
			// 文字列が終わるまで繰り返し処理
			while (in.unlessEof()) {
				// =記号が現れるまで読み取り
				final String name = helper.parseUntil(in, '=');
				// =記号の次に移動
				// 現在文字を確認
				if (in.next() == ('"')) {
					// ダブルクオテーションだったら改行を含む文字列としてパース
					varList.add(new EnvironmentVariableImpl(name, helper.parseQuotedString(in)));
				} else {
					// それ以外の場合は改行を含まない文字列としてパース
					varList.add(new EnvironmentVariableImpl(name, helper.parseUntil(in, '\r', '\n')));
				}
				// 改行文字その他の空白をスキップ
				helper.skipWhitespace(in);			
			}
			return ParseResult.successful(ListUtils.immutableList(varList));
		} catch (final InputExeption e) {
			return ParseResult.failure(new ParseException(e, in));
		} catch (final ParseException e) {
			return ParseResult.failure(e);
		}
	}
}
