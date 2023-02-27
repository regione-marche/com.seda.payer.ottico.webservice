package com.seda.payer.ottico.facade.unittest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class TestLogElaborazioneImg {

	public static void main(String[] args) {
		//we define filename
		String fileName = "D:\\FileTemporanei\\Payer\\Ottico\\log\\DET00004000TO069542011-06-131308841945273__MM.log";
		try {
			String appo = "dshkjhdfghkdfhghdfhgkjdhghdkjfhgkjdhf\nsdsjgkjgkldjfjgkldfg\ndsgjkldfjgkljdgjdfg\ndfkgldfjgjdfgjldfjg\n";
			String[] split = appo.split("\n");
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(fileName), false));
			for (int i = 0; i < split.length; i++) {
				writer.println(split[i]);
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}