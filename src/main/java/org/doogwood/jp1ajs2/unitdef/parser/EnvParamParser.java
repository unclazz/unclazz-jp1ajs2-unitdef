package org.doogwood.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.List;

import org.doogwood.jp1ajs2.unitdef.EnvironmentVariable;
import org.doogwood.jp1ajs2.unitdef.parser.Parsers.Options;

public final class EnvParamParser extends AbstractParser<List<EnvironmentVariable>> {

	private static final Options OPTIONS = new Options();
	static {
		OPTIONS.setEscapePrefixInDoubleQuotes('#');
	}
	
	public EnvParamParser() {
		super(OPTIONS);
	}
	
	public List<EnvironmentVariable> parse(final Input in) {
		try {
			final List<EnvironmentVariable> env = new ArrayList<EnvironmentVariable>();
			// 空白をトリム
			parsers.skipWhitespace(in);
			// 文字列が終わるまで繰り返し処理
			while (in.unlessEof()) {
				// =記号が現れるまで読み取り
				final String name = parsers.parseUntil(in, '=');
				// =記号の次に移動
				// 現在文字を確認
				if (in.next() == ('"')) {
					// ダブルクオテーションだったら改行を含む文字列としてパース
					env.add(new EnvironmentVariableImpl(name, parsers.parseQuotedString(in)));
				} else {
					// それ以外の場合は改行を含まない文字列としてパース
					env.add(new EnvironmentVariableImpl(name, parsers.parseUntil(in, '\r', '\n')));
				}
				// 改行文字その他の空白をスキップ
				parsers.skipWhitespace(in);			
			}
			return env;
		} catch (InputExeption e) {
			throw new ParseException(e, in);
		}
	}
}
