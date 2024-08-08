package org.example.user.common;

import org.springframework.stereotype.Component;

@Component
public class RandomName {

	private String sNickName;

	public String getsNickName() {
		return sNickName;
	}

	public String setsNickName() {
		StringBuffer bNick = new StringBuffer();
		int iNameSize = 3;

		for (int i = 0; i < iNameSize; i++) {
			char ch = (char)((Math.random() * 11172) + 0xAC00);

			bNick.append(ch);
			this.sNickName = bNick.toString();
		}
		return sNickName;
	}
}
