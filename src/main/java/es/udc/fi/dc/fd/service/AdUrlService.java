package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.AdUrl;

public interface AdUrlService {

	AdUrl createAdUrl(AdUrl adUrl);

	AdUrl findAdImage(Ad ad, long imageId);

}
