import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './loan-subscription.reducer';

export const LoanSubscriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const loanSubscriptionEntity = useAppSelector(state => state.loanSubscription.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loanSubscriptionDetailsHeading">
          <Translate contentKey="smartAppleApp.loanSubscription.detail.title">LoanSubscription</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loanSubscriptionEntity.id}</dd>
          <dt>
            <span id="hash">
              <Translate contentKey="smartAppleApp.loanSubscription.hash">Hash</Translate>
            </span>
          </dt>
          <dd>{loanSubscriptionEntity.hash}</dd>
          <dt>
            <span id="subAmt">
              <Translate contentKey="smartAppleApp.loanSubscription.subAmt">Sub Amt</Translate>
            </span>
          </dt>
          <dd>{loanSubscriptionEntity.subAmt}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="smartAppleApp.loanSubscription.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>
            {loanSubscriptionEntity.createAt ? (
              <TextFormat value={loanSubscriptionEntity.createAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updateAt">
              <Translate contentKey="smartAppleApp.loanSubscription.updateAt">Update At</Translate>
            </span>
          </dt>
          <dd>
            {loanSubscriptionEntity.updateAt ? (
              <TextFormat value={loanSubscriptionEntity.updateAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="smartAppleApp.loanSubscription.subscriber">Subscriber</Translate>
          </dt>
          <dd>{loanSubscriptionEntity.subscriber ? loanSubscriptionEntity.subscriber.id : ''}</dd>
          <dt>
            <Translate contentKey="smartAppleApp.loanSubscription.loan">Loan</Translate>
          </dt>
          <dd>{loanSubscriptionEntity.loan ? loanSubscriptionEntity.loan.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/loan-subscription" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loan-subscription/${loanSubscriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoanSubscriptionDetail;
