package com.seda.payer.ottico.facade.core;

import java.io.Serializable;
import com.seda.payer.core.bean.Configurazione;
import com.seda.payer.ottico.webservice.dto.DirectoryInfo;

public class ElaboraFlussiImgHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	public static DirectoryInfo getDirectoryInfo(Configurazione conf) {
		return new DirectoryInfo()
				.setDirectoryFlussiDatiOtticoInput(conf.getDirectoryFlussiDatiOtticoInput())
				.setDirectorySalvataggoFlussiDatiOtticoInput(conf.getDirectorySalvataggoFlussiDatiOtticoInput())
				.setDirectoryFlussiImmaginiOtticoInput(conf.getDirectoryFlussiImmaginiOtticoInput())
				.setDirectorySalvataggioFlussiImmaginiOtticoInput(conf.getDirectorySalvataggioFlussiImmaginiOtticoInput())
				.setDirectoryImmaginiOtticoPerEstrattoConto(conf.getDirectoryImmaginiOtticoPerEstrattoConto())
				.setDirectoryLogElaborazione(conf.getDirectoryLogElaborazione());
	}
}
