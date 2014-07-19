package usertools.jp1ajs2.unitdef.util;

import java.util.ArrayList;
import java.util.List;
import usertools.jp1ajs2.unitdef.ext.EnvironmentVariable;
import com.m12i.code.parse.ParseException;
import com.m12i.code.parse.ParserTemplate;

class EnvParamParser extends ParserTemplate<List<EnvironmentVariable>> {

	@Override
	protected List<EnvironmentVariable> parseMain() throws ParseException {
		return parseEnv();
	}

	private List<EnvironmentVariable> parseEnv() throws ParseException {
		final List<EnvironmentVariable> env = new ArrayList<EnvironmentVariable>();
		// 空白をトリム
		skipSpace();
		// 文字列が終わるまで繰り返し処理
		while (!hasReachedEof()) {
			// =記号が現れるまで読み取り
			final String name = parseUntil('=');
			// =記号の次に移動
			next();
			// 現在文字を確認
			if (currentIs('"')) {
				// ダブルクオテーションだったら改行を含む文字列としてパース
				env.add(new EnvironmentVariable(name, parseQuotedString()));
			} else {
				// それ以外の場合は改行を含まない文字列としてパース
				env.add(new EnvironmentVariable(name, parseUntil('\r', '\n')));
			}
			// 改行文字その他の空白をスキップ
			skipSpace();			
		}
		return env;
	}

	@Override
	public char escapePrefixInSingleQuotes() {
		// シングルクオテーションで囲われた文字列はサポートしない
		return '\u0000';
	}

	@Override
	public char escapePrefixInDoubleQuotes() {
		// ダブルクオテーションで囲われた文字列のエスケープ文字は#
		return '#';
	}

	@Override
	public String lineCommentStart() {
		// コメントはサポートしない
		return null;
	}

	@Override
	public String blockCommentStart() {
		// コメントはサポートしない
		return null;
	}

	@Override
	public String blockCommentEnd() {
		return null;
	}

	@Override
	public boolean skipCommentWithSpace() {
		// コメントはサポートしない
		return false;
	}

}
