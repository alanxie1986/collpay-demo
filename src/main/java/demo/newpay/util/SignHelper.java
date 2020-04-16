package demo.newpay.util;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class SignHelper {

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	// MD5Util工具类中相关的方法
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String md5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception e) {

		}
		return resultString;
	}

	public static String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		String ggcrea = sb.toString();
		ggcrea = ggcrea.substring(0, ggcrea.length() - 1);
		String sign = md5Encode(ggcrea, "utf-8");
		return sign;
	}


	//获取签名
	public static String getMd5Sign(Map<String, String> signParams, String apiKey) {
		signParams.remove("sign");
		String StringA = formatUrlMap(signParams, false, true);
		String stringSignTemp = SignHelper.md5Encode(StringA + "&key=" + apiKey, "utf-8").toUpperCase();
		return stringSignTemp;
	}


	//生成随机字符
	public static String nonceStr() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(2);
	}

	/**
	 *
	 * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 *
	 * @param paraMap   要排序的Map对象
	 * @param urlEncode   是否需要URLENCODE
	 * @param keyToLower    是否需要将Key转换为全小写
	 *            true:key转化成小写，false:不转化
	 * @return
	 */
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
	{
		if(paraMap == null){
			return "";
		}
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try
		{
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
			{
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
				{
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds)
			{
				String key = item.getKey();
				String val = item.getValue();

				//屏蔽平台的context
				if (key.equals("context")) {
					continue;
				}
				if (urlEncode)
				{
					val = URLEncoder.encode(val, "utf-8");
				}
				if (keyToLower)
				{
					buf.append(key.toLowerCase() + "=" + val);
				} else
				{
					buf.append(key + "=" + val);
				}
				buf.append("&");
			}
			buff = buf.toString();
			if (buff.isEmpty() == false)
			{
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e)
		{
			return null;
		}
		return buff;
	}
}
