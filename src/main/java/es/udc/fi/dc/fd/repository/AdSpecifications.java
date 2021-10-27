package es.udc.fi.dc.fd.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.User;

public class AdSpecifications {

	public static Specification<Ad> findAdCustomSearch(String city, String keywords, LocalDate date_start,
			LocalDate date_end, BigDecimal price_min, BigDecimal price_max, float min_val) {

		return new Specification<Ad>() {

			private static final long serialVersionUID = 5551230972464582893L;

			@Override
			public Predicate toPredicate(Root<Ad> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				Join<Ad, User> joinAdUser = root.join("user", JoinType.INNER);

				List<Predicate> predicates = new ArrayList<Predicate>();
				String[] words = keywords != null ? keywords.split(" ") : null;

				if (city != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")),
							"%" + city.toLowerCase() + "%")));
				}

				if (words != null && words.length > 0) {
					for (int i = 0; i < words.length; i++) {
						predicates.add(criteriaBuilder.and(criteriaBuilder
								.like(criteriaBuilder.lower(root.get("adName")), "%" + words[i].toLowerCase() + "%")));
					}
				}

				if (date_start != null && date_end != null) {
					predicates
							.add(criteriaBuilder.and(criteriaBuilder.between(root.get("date"), date_start, date_end)));

				} else if (date_start != null && date_end == null) {
					predicates.add(criteriaBuilder
							.and(criteriaBuilder.between(root.get("date"), date_start, LocalDate.now())));

				} else if (date_end != null && date_start == null) {
					predicates.add(criteriaBuilder
							.and(criteriaBuilder.between(root.get("date"), LocalDate.now().withYear(2000), date_end)));
				}

				if (price_min != null && price_max == null) {
					predicates.add(
							criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price_min)));

				} else if (price_min == null && price_max != null) {
					predicates
							.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("price"), price_max)));

				} else if (price_min != null && price_max != null) {
					predicates
							.add(criteriaBuilder.and(criteriaBuilder.between(root.get("price"), price_min, price_max)));
				}

				if (min_val > 0) {
					predicates.add(
							criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(joinAdUser.get("rate"), min_val)));

				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}

		};
	}

}
