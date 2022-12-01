import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './loan.reducer';

export const LoanDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const loanEntity = useAppSelector(state => state.loan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loanDetailsHeading">
          <Translate contentKey="smartAppleApp.loan.detail.title">Loan</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loanEntity.id}</dd>
          <dt>
            <span id="hash">
              <Translate contentKey="smartAppleApp.loan.hash">Hash</Translate>
            </span>
          </dt>
          <dd>{loanEntity.hash}</dd>
          <dt>
            <span id="loanAmt">
              <Translate contentKey="smartAppleApp.loan.loanAmt">Loan Amt</Translate>
            </span>
          </dt>
          <dd>{loanEntity.loanAmt}</dd>
          <dt>
            <span id="availableAmt">
              <Translate contentKey="smartAppleApp.loan.availableAmt">Available Amt</Translate>
            </span>
          </dt>
          <dd>{loanEntity.availableAmt}</dd>
          <dt>
            <span id="interestRate">
              <Translate contentKey="smartAppleApp.loan.interestRate">Interest Rate</Translate>
            </span>
          </dt>
          <dd>{loanEntity.interestRate}</dd>
          <dt>
            <span id="terms">
              <Translate contentKey="smartAppleApp.loan.terms">Terms</Translate>
            </span>
          </dt>
          <dd>{loanEntity.terms}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="smartAppleApp.loan.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>{loanEntity.createAt ? <TextFormat value={loanEntity.createAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updateAt">
              <Translate contentKey="smartAppleApp.loan.updateAt">Update At</Translate>
            </span>
          </dt>
          <dd>{loanEntity.updateAt ? <TextFormat value={loanEntity.updateAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="smartAppleApp.loan.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{loanEntity.currency}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="smartAppleApp.loan.status">Status</Translate>
            </span>
          </dt>
          <dd>{loanEntity.status}</dd>
          <dt>
            <Translate contentKey="smartAppleApp.loan.owner">Owner</Translate>
          </dt>
          <dd>{loanEntity.owner ? loanEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/loan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loan/${loanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoanDetail;
