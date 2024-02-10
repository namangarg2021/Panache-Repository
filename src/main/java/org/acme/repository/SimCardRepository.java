package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.enitty.SimCard;

@ApplicationScoped
public class SimCardRepository implements PanacheRepository<SimCard> {
}
