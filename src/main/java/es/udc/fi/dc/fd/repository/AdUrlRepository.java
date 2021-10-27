package es.udc.fi.dc.fd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.udc.fi.dc.fd.model.persistence.AdUrl;
import es.udc.fi.dc.fd.model.persistence.AdUrlPK;

@Repository
public interface AdUrlRepository extends JpaRepository<AdUrl, AdUrlPK> {

}
