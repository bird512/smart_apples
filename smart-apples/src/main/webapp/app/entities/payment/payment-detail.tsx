import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment.reducer';

export const PaymentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentEntity = useAppSelector(state => state.payment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentDetailsHeading">
          <Translate contentKey="smartAppleApp.payment.detail.title">Payment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.id}</dd>
          <dt>
            <span id="hash">
              <Translate contentKey="smartAppleApp.payment.hash">Hash</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.hash}</dd>
          <dt>
            <span id="interest">
              <Translate contentKey="smartAppleApp.payment.interest">Interest</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.interest}</dd>
          <dt>
            <span id="principal">
              <Translate contentKey="smartAppleApp.payment.principal">Principal</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.principal}</dd>
          <dt>
            <span id="createAt">
              <Translate contentKey="smartAppleApp.payment.createAt">Create At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.createAt ? <TextFormat value={paymentEntity.createAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updateAt">
              <Translate contentKey="smartAppleApp.payment.updateAt">Update At</Translate>
            </span>
          </dt>
          <dd>{paymentEntity.updateAt ? <TextFormat value={paymentEntity.updateAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="smartAppleApp.payment.subscription">Subscription</Translate>
          </dt>
          <dd>{paymentEntity.subscription ? paymentEntity.subscription.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/payment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment/${paymentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentDetail;
