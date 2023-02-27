package com.seda.payer.ottico.webservice.source;

import com.seda.payer.ottico.webservice.dati.PresenzaFlussoTypeKeys;

public class _TestService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			boolean appo = false;

			System.out.println(PresenzaFlussoTypeKeys.parse(String.valueOf(appo)));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
