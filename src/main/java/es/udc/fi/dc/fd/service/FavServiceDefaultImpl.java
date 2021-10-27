package es.udc.fi.dc.fd.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Fav;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.FavRepository;

@Service
@Transactional
public class FavServiceDefaultImpl implements FavService {

	@Autowired
	private FavRepository favRepository;

	@Autowired
	private AdRepository adRepository;

	@Override
	public Fav addFav(Fav fav) {

		Long id = favRepository.save(fav).getId();
		Fav newFav = favRepository.getOne(id);
		return favRepository.save(newFav);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> showFavs(String userName) {

		List<Fav> favList = favRepository.findByUserName(userName);

		List<Long> fv = new ArrayList<Long>();

		for (int i = 0; i <= favList.size() - 1; i++)
			fv.add(favList.get(i).getAdId());

		return fv;
	}

	@Override
	public void remove(Long adId, String userName) {
		favRepository.deleteByAdIdAndUserName(adId, userName);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Ad> showAdsFavs(String userName) {

		List<Fav> favList = favRepository.findByUserName(userName);

		List<Ad> fv = new ArrayList<Ad>();

		for (int i = 0; i <= favList.size() - 1; i++)
			fv.add(adRepository.findById(favList.get(i).getAdId()).get());

		return fv;
	}

}
