package es.udc.fi.dc.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.AdUrlPK;
import es.udc.fi.dc.fd.repository.AdUrlRepository;

@Service
@Transactional
public class AdUrlServiceDefaultImpl implements AdUrlService {

	@Autowired
	private AdUrlRepository adUrlRepository;

	@Override
	public AdUrl createAdUrl(AdUrl adUrl) {

		return adUrlRepository.save(adUrl);

	}

	@Override
	public AdUrl findAdImage(Ad ad, long imageId) {

		return adUrlRepository.findById(new AdUrlPK(ad, imageId)).get();
	}

}
