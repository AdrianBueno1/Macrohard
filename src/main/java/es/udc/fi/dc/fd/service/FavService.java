package es.udc.fi.dc.fd.service;

import java.util.List;

import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Fav;

public interface FavService {

	Fav addFav(Fav fav);

	void remove(Long adId, String userName);

	List<Long> showFavs(String userName);

	List<Ad> showAdsFavs(String userName);
}
