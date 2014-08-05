package usertools.jp1ajs2.unitdef.core;

import java.util.ArrayList;
import java.util.List;

import unklazz.parsec.Parser;
import unklazz.parsec.Reader;
import unklazz.parsec.Result;
import unklazz.parsec.parsers.Abc123;
import static usertools.jp1ajs2.unitdef.core.UnitP.*;

final class ParamP implements Parser<Param> {

	private static final Abc123 nameP = Abc123.abc123();
	private static final ParamValueP valueP = new ParamValueP();
	
	@Override
	public final Result<Param> parse(Reader in) {
		final Result<String> nameR = nameP.parse(in);
		// チェック
		if (nameR.isFailure() || nameR.get().isEmpty()) error("ユニット定義パラメータにはパラメータ名が必要.", in);
		if (in.current() != '=') error("ユニット定義パラメータの名称のあとには'='が必要.",in);
		
		// パラメータ値を一時的に格納するリストを初期化
		final List<ParamValue> values = new ArrayList<ParamValue>();
		
		// パラメータの終端文字';'が登場するまで繰り返し
		char c = in.current();
		while (c != ';') {
			// '='や','を読み飛ばして前進
			in.next();
			// パラメータ値をパース
			final Result<ParamValue> valueR = valueP.parse(in);
			// チェック
			if (valueR.isFailure()) error("ユニット定義パラメータのパースに失敗.", in);
			// パラメータ値をリストに格納
			values.add(valueR.get());
			// パラメータ値読取り後にもかかわらず現在文字が区切り文字以外であれば構文エラー
			c = in.current();
			if (c != ',' && c != ';') error("", in);
		}
		// 読取った結果を使ってパラメータを初期化して返す
		final Param p = new ParamImpl(nameR.get(), values);
		return Result.success(p);
	}

	@Override
	public final String tokenName() {
		return "jp1/ajs2-unit-definition-parameter";
	}

}
