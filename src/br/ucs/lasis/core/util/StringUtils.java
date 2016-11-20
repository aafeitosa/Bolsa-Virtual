package br.ucs.lasis.core.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class StringUtils {
	
	public static String getHash(String value) {
		
		return Hashing.sha256()
		        .hashString(value, Charsets.UTF_8)
		        .toString();
	}
	
	public static void main(String[] args) {
		
		System.out.println(getHash("alessandrofeitosa"));
		
	}

}
