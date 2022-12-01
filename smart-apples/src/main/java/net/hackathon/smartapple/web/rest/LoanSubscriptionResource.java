package net.hackathon.smartapple.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.hackathon.smartapple.repository.LoanSubscriptionRepository;
import net.hackathon.smartapple.service.LoanSubscriptionService;
import net.hackathon.smartapple.service.dto.LoanSubscriptionDTO;
import net.hackathon.smartapple.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link net.hackathon.smartapple.domain.LoanSubscription}.
 */
@RestController
@RequestMapping("/api")
public class LoanSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(LoanSubscriptionResource.class);

    private static final String ENTITY_NAME = "loanSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanSubscriptionService loanSubscriptionService;

    private final LoanSubscriptionRepository loanSubscriptionRepository;

    public LoanSubscriptionResource(
        LoanSubscriptionService loanSubscriptionService,
        LoanSubscriptionRepository loanSubscriptionRepository
    ) {
        this.loanSubscriptionService = loanSubscriptionService;
        this.loanSubscriptionRepository = loanSubscriptionRepository;
    }

    /**
     * {@code POST  /loan-subscriptions} : Create a new loanSubscription.
     *
     * @param loanSubscriptionDTO the loanSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanSubscriptionDTO, or with status {@code 400 (Bad Request)} if the loanSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-subscriptions")
    public ResponseEntity<LoanSubscriptionDTO> createLoanSubscription(@Valid @RequestBody LoanSubscriptionDTO loanSubscriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save LoanSubscription : {}", loanSubscriptionDTO);
        if (loanSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanSubscriptionDTO result = loanSubscriptionService.save(loanSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/loan-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-subscriptions/:id} : Updates an existing loanSubscription.
     *
     * @param id the id of the loanSubscriptionDTO to save.
     * @param loanSubscriptionDTO the loanSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the loanSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-subscriptions/{id}")
    public ResponseEntity<LoanSubscriptionDTO> updateLoanSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanSubscriptionDTO loanSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanSubscription : {}, {}", id, loanSubscriptionDTO);
        if (loanSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanSubscriptionDTO result = loanSubscriptionService.update(loanSubscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-subscriptions/:id} : Partial updates given fields of an existing loanSubscription, field will ignore if it is null
     *
     * @param id the id of the loanSubscriptionDTO to save.
     * @param loanSubscriptionDTO the loanSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the loanSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-subscriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanSubscriptionDTO> partialUpdateLoanSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanSubscriptionDTO loanSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanSubscription partially : {}, {}", id, loanSubscriptionDTO);
        if (loanSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanSubscriptionDTO> result = loanSubscriptionService.partialUpdate(loanSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-subscriptions} : get all the loanSubscriptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanSubscriptions in body.
     */
    @GetMapping("/loan-subscriptions")
    public List<LoanSubscriptionDTO> getAllLoanSubscriptions() {
        log.debug("REST request to get all LoanSubscriptions");
        return loanSubscriptionService.findAll();
    }

    /**
     * {@code GET  /loan-subscriptions/:id} : get the "id" loanSubscription.
     *
     * @param id the id of the loanSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-subscriptions/{id}")
    public ResponseEntity<LoanSubscriptionDTO> getLoanSubscription(@PathVariable Long id) {
        log.debug("REST request to get LoanSubscription : {}", id);
        Optional<LoanSubscriptionDTO> loanSubscriptionDTO = loanSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanSubscriptionDTO);
    }

    /**
     * {@code DELETE  /loan-subscriptions/:id} : delete the "id" loanSubscription.
     *
     * @param id the id of the loanSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-subscriptions/{id}")
    public ResponseEntity<Void> deleteLoanSubscription(@PathVariable Long id) {
        log.debug("REST request to delete LoanSubscription : {}", id);
        loanSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
