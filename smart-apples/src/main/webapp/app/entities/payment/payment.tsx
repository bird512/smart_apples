import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPayment } from 'app/shared/model/payment.model';
import { getEntities } from './payment.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';

export const Payment = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const paymentList = useAppSelector(state => state.payment.entities);
  const loading = useAppSelector(state => state.payment.loading);
  const users = useAppSelector(state => state.userManagement.users);
  const [userMap, setUserMap] = useState({});
  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getUsers({}));
  }, []);
  useEffect(() => {
    let obj = {};
    users.forEach(element => {
      obj[element.id] = element.login;
    });
    console.log(obj, 'obj====');
    setUserMap(obj);
  }, [users]);
  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="payment-heading" data-cy="PaymentHeading">
        <Translate contentKey="smartAppleApp.payment.home.title">Payments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartAppleApp.payment.home.refreshListLabel">Refresh List</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {paymentList && paymentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="smartAppleApp.payment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.payment.hash">Hash</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.payment.interest">Interest</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.payment.principal">Principal</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.payment.createAt">Create At</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.payment.updateAt">Update At</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.payment.subscription">Subscription</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paymentList.map((payment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/payment/${payment.id}`} color="link" size="sm">
                      {payment.id}
                    </Button>
                  </td>
                  <td>{payment.hash}</td>
                  <td>{payment.interest}</td>
                  <td>{payment.principal}</td>
                  <td>{payment.createAt ? <TextFormat type="date" value={payment.createAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{payment.updateAt ? <TextFormat type="date" value={payment.updateAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {payment.subscription ? (
                      <Link to={`/loan-subscription/${payment.subscription.id}`}>
                        {userMap[payment.subscription.id] || payment.subscription.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/payment/${payment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="smartAppleApp.payment.home.notFound">No Payments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Payment;
