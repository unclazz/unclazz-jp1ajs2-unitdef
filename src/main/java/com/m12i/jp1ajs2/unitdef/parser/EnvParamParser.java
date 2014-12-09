package com.m12i.jp1ajs2.unitdef.parser;

import java.util.ArrayList;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.ext.EnvironmentVariable;
import com.m12i.jp1ajs2.unitdef.parser.Parsers.Options;

public class EnvParamParser {

	private final Parsers coreParsers;
	
	public EnvParamParser() {
		final Options options = new Options();
		options.setEscapePrefixInDoubleQuotes('#');
		coreParsers = new Parsers(options);
	}
	
	public List<EnvironmentVariable> parse(final Reader in) {
		final List<EnvironmentVariable> env = new ArrayList<EnvironmentVariable>();
		// 空白をトリム
		coreParsers.skipWhitespace(in);
		// 文字列が終わるまで繰り返し処理
		while (!in.hasReachedEof()) {
			// =記号が現れるまで読み取り
			final String name = coreParsers.parseUntil(in, '=').value;
			// =記号の次に移動
			// 現在文字を確認
			if (in.next() == ('"')) {
				// ダブルクオテーションだったら改行を含む文字列としてパース
				env.add(new EnvironmentVariable(name, coreParsers.parseQuotedString(in).value));
			} else {
				// それ以外の場合は改行を含まない文字列としてパース
				env.add(new EnvironmentVariable(name, coreParsers.parseUntil(in, '\r', '\n').value));
			}
			// 改行文字その他の空白をスキップ
			coreParsers.skipWhitespace(in);			
		}
		return env;
	}
}
