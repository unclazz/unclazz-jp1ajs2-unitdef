package com.m12i.jp1ajs2.unitdef;

/**
 * ユニット種別.
 */
public enum UnitType {
	/** ジョブグループまたはプランニンググループ. */
	GROUP("g", "g：ジョブグループまたはプランニンググループ", false),
	/** マネージャージョブグループ. */
	MANAGER_JOB_GROUP("mg", "mg：マネージャージョブグループ", false),
	/** ジョブネット. */
	JOBNET("n", "n：ジョブネット", false),
	/** リカバリージョブネット. */
	RECOVERY_JOBNET("rn", "rn：リカバリージョブネット", true),
	/** リモートジョブネット. */
	REMOTE_JOBNET("rm", "rm：リモートジョブネット", false),
	/** リカバリーリモートジョブネット. */
	RECOVERY_REMOTE_JOBNET("rr", "rr：リカバリーリモートジョブネット", true),
	/** ルートジョブネット起動条件. */
	ROOT_JOBNET_INVOKE_CONDITION("rc", "rc：ルートジョブネットの起動条件", false),
	/** マネージャージョブネット. */
	MANAGER_JOBNET("mn", "mn：マネージャージョブネット", false),
	/** UNIXジョブ. */
	UNIX_JOB("j", "j：UNIXジョブ", false),
	/** リカバリーUNIXジョブ. */
	RECOVERY_UNIX_JOB("rj", "rj：リカバリーUNIXジョブ", true),
	/** PCジョブ. */
	PC_JOB("pj", "pj：PCジョブ", false),
	/** リカバリーPCジョブ. */
	RECOVERY_PC_JOB("rp", "rp：リカバリーPCジョブ", true),
	/** QUEUEジョブ. */
	QUEUE_JOB("qj", "qj：QUEUEジョブ", false),
	/** . */
	RECOVERY_QUEUE_JOB("rq", "rq：リカバリーQUEUEジョブ", true),
	/** リカバリーQUEUEジョブ. */
	JUDGE_JOB("jdj", "jdj：判定ジョブ", false),
	/** リカバリー判定ジョブ. */
	RECOVERY_JUDGE_JOB("rjdj", "rjdj：リカバリー判定ジョブ", true),
	/** ORジョブ. */
	OR_JOB("orj", "orj：ORジョブ", false),
	/** リカバリーORジョブ. */
	RECOVERY_OR_JOB("rorj", "rorj：リカバリーORジョブ", true),
	/** JP1イベント受信監視ジョブ. */
	JP1EVENT_WATCH_JOB("evwj", "evwj：JP1イベント受信監視ジョブ", false),
	/** リカバリーJP1イベント受信監視ジョブ. */
	RECOVERY_JP1EVENT_WATCH_JOB("revwj", "revwj：リカバリーJP1イベント受信監視ジョブ", true),
	/** ファイル監視ジョブ. */
	FILE_WATCH_JOB("flwj", "flwj：ファイル監視ジョブ", false),
	/** リカバリーファイル監視ジョブ. */
	RECOVERY_FILE_WATCH_JOB("rflwj", "rflwj：リカバリーファイル監視ジョブ", true),
	/** メール受信監視ジョブ. */
	MAIL_WATCH_JOB("mlwj", "mlwj：メール受信監視ジョブ", false),
	/** リカバリーメール受信監視ジョブ. */
	RECOVERY_MAIL_WATCH_JOB("rmlwj", "rmlwj：リカバリーメール受信監視ジョブ", true),
	/** メッセージキュー受信監視ジョブ. */
	MESSAGE_QUEUE_WATCH_JOB("mqwj", "mqwj：メッセージキュー受信監視ジョブ", false),
	/** リカバリーメッセージキュー受信監視ジョブ. */
	RECOVERY_MESSAGE_QUEUE_WATCH_JOB("rmqwj", "rmqwj：リカバリーメッセージキュー受信監視ジョブ", true),
	/** MSMQ受信監視ジョブ. */
	MSMQ_WATCH_JOB("mswj", "mswj：MSMQ受信監視ジョブ", false),
	/** リカバリーMSMQ受信監視ジョブ. */
	RECOVERY_MSMQ_WATCH_JOB("rmswj", "rmswj：リカバリーMSMQ受信監視ジョブ", true),
	/** リカバリーMSMQ受信監視ジョブ. */
	LOG_FILE_WATCH_JOB("lfwj", "lfwj：ログファイル監視ジョブ", false),
	/** リカバリーログファイル監視ジョブ. */
	RECOVERY_LOG_FILE_WATCH_JOB("rlfwj", "rlfwj：リカバリーログファイル監視ジョブ", true),
	/** Windowsイベントログ監視ジョブ. */
	WINDOWS_EVENTLOG_WATCH_JOB("ntwj", "ntwj：Windowsイベントログ監視ジョブ", false),
	/** リカバリーWindowsイベントログ監視ジョブ. */
	RECOVERY_WINDOWS_EVENTLOG_WATCH_JOB("rntwj", "rntwj：リカバリーWindowsイベントログ監視ジョブ", true),
	/** 実行間隔制御ジョブ. */
	TIME_TO_WAIT_JOB("tmwj", "tmwj：実行間隔制御ジョブ", false),
	/** リカバリー実行間隔制御ジョブ. */
	RECOVERY_TIME_TO_WAIT_JOB("rtmwj", "rtmwj：リカバリー実行間隔制御ジョブ", true),
	/** JP1イベント送信ジョブ. */
	JP1EVENT_SEND_JOB("evsj", "evsj：JP1イベント送信ジョブ", false),
	/** リカバリーJP1イベント送信ジョブ. */
	RECOVERY_JP1EVENT_SEND_JOB("revsj", "revsj：リカバリーJP1イベント送信ジョブ", true),
	/** メール送信ジョブ. */
	MAIL_SEND_JOB("mlsj", "mlsj：メール送信ジョブ", false),
	/** リカバリーメール送信ジョブ. */
	RECOVERY_MAIL_SEND_JOB("rmlsj", "rmlsj：リカバリーメール送信ジョブ", true),
	/** メッセージキュー送信ジョブ. */
	MESSAGE_QUEUE_SEND_JOB("mqsj", "mqsj：メッセージキュー送信ジョブ", false),
	/** リカバリーメッセージキュー送信ジョブ. */
	RECOVERY_MESSAGE_QUEUE_SEND_JOB("rmqsj", "rmqsj：リカバリーメッセージキュー送信ジョブ", true),
	/** MSMQ送信ジョブ. */
	MSMQ_SEND_JOB("mssj", "mssj：MSMQ送信ジョブ", false),
	/** リカバリーMSMQ送信ジョブ. */
	RECOVERY_MSMQ_SEND_JOB("rmssj", "rmssj：リカバリーMSMQ送信ジョブ", true),
	/** JP1/Cm2状態通知ジョブ. */
	CM2_STATE_SEND_JOB("cmsj", "cmsj：JP1/Cm2状態通知ジョブ", false),
	/** リカバリーJP1/Cm2状態通知ジョブ. */
	RECOVERY_CM2_STATE_SEND_JOB("rcmsj", "rcmsj：リカバリーJP1/Cm2状態通知ジョブ", true),
	/** ローカル電源制御ジョブ. */
	POWER_CONTROL_JOB("pwlj", "pwlj：ローカル電源制御ジョブ", false),
	/** リカバリーローカル電源制御ジョブ. */
	RECOVERY_POWER_CONTROL_JOB("rpwlj", "rpwlj：リカバリーローカル電源制御ジョブ", true),
	/** リモート電源制御ジョブ. */
	REMOTE_POWER_CONTROL_JOB("pwrj", "pwrj：リモート電源制御ジョブ", false),
	/** リカバリーリモート電源制御ジョブ. */
	RECOVERY_REMOTE_POWER_CONTROL_JOB("rpwrj", "rpwrj：リカバリーリモート電源制御ジョブ", true),
	/** カスタムUNIXジョブ. */
	CUSTOM_UNIX_JOB("cj", "cj：カスタムUNIXジョブ", false),
	/** リカバリーカスタムUNIXジョブ. */
	RECOVERY_CUSTOM_UNIX_JOB("rcj", "rcj：リカバリーカスタムUNIXジョブ", true),
	/** カスタムPCジョブ. */
	CUSTOM_PC_JOB("cpj", "cpj：カスタムPCジョブ", false),
	/** リカバリーカスタムPCジョブ. */
	RECOVERY_CUSTOM_PC_JOB("rcpj", "rcpj：リカバリーカスタムPCジョブ", true),
	/** ホストリンクジョブネット. */
	HOST_LINK_JOBNET("hln", "hln：ホストリンクジョブネット", false),
	/** ジョブネットコネクタ. */
	JOBNET_CONNECTOR("nc", "nc：ジョブネットコネクタ", false);
	
	private String code;
	private String desc;
	private boolean recovery;
	
	private UnitType(final String code, final String desc, final boolean recovery){
		this.code = code;
		this.desc = desc;
		this.recovery = recovery;
	}
	
	/**
	 * JP1ユニット定義で使用されるコードを返す.
	 * @return コード
	 */
	public final String getCode(){
		return code;
	}
	/**
	 * JP1ユニット定義の説明テキストを返す.
	 * @return 説明テキスト
	 */
	public final String getDescription(){
		return desc;
	}
	/**
	 * ユニット種別がリカバリー系であるかどうかを返す.
	 * @return {@code true}：リカバリー系ユニット種別、{@code false}：非リカバリー系ユニット種別
	 */
	public final boolean isRecoveryType(){
		return recovery;
	}
	/**
	 * 同種の正常系（非リカバリー系）ユニット種別を返す.
	 * レシーバ・オブジェクトが正常系のユニット種別の場合はそれ自身を返す。
	 * レシーバ・オブジェクトがリカバリー系ユニット種別の場合は同種の正常系ユニット種別を返す。
	 * @return ユニット種別
	 */
	public final UnitType getNormalType() {
		return this.isRecoveryType() ? forCode(this.code.replaceAll("^r", "")) : this;
	}
	/**
	 * 同種のリカバリー系ユニット種別を返す.
	 * レシーバ・オブジェクトがリカバリー系ユニット種別の場合はそれ自身を返す。
	 * レシーバ・オブジェクトが正常系のユニット種別の場合は同種のリカバリー系ユニット種別を返す。
	 * @return ユニット種別
	 */
	public final UnitType getRecoveryType() {
		return this.isRecoveryType() ? this : forCode("r" + this.code);
	}
	/**
	 * コードをキーとして列挙体インスタンスを検索して返す.
	 * @param code コード
	 * @return ユニット種別
	 */
	public static final UnitType forCode(final String code){
		for(final UnitType t : values()){
			if(t.code.equals(code)){
				return t;
			}
		}
		return null;
	}
}
