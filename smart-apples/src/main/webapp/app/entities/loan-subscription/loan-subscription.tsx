import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILoanSubscription } from 'app/shared/model/loan-subscription.model';
import { getEntities } from './loan-subscription.reducer';

export const LoanSubscription = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const loanSubscriptionList = useAppSelector(state => state.loanSubscription.entities);
  const loading = useAppSelector(state => state.loanSubscription.loading);
  const users = useAppSelector(state => state.userManagement.users);
  const [userMap, setUserMap] = useState({});
  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getUsers({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  useEffect(() => {
    let obj = {};
    users.forEach(element => {
      obj[element.id] = element.login;
    });
    setUserMap(obj);
  }, [users]);

  return (
    <div>
      <h2 id="loan-subscription-heading" data-cy="LoanSubscriptionHeading">
        <Translate contentKey="smartAppleApp.loanSubscription.home.title">Loan Subscriptions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartAppleApp.loanSubscription.home.refreshListLabel">Refresh List</Translate>
          </Button>
          {/* <Link to="/loan-subscription/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="smartAppleApp.loanSubscription.home.createLabel">Create new Loan Subscription</Translate>
          </Link> */}
        </div>
      </h2>
      <div className="table-responsive">
        {loanSubscriptionList && loanSubscriptionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.hash">Hash</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.subAmt">Sub Amt</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.createAt">Create At</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.updateAt">Update At</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.subscriber">Subscriber</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loanSubscription.loan">Loan</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {loanSubscriptionList.map((loanSubscription, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/loan-subscription/${loanSubscription.id}`} color="link" size="sm">
                      {loanSubscription.id}
                    </Button>
                  </td>
                  <td>{loanSubscription.hash}</td>
                  <td>{loanSubscription.subAmt}</td>
                  <td>
                    {loanSubscription.createAt ? (
                      <TextFormat type="date" value={loanSubscription.createAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {loanSubscription.updateAt ? (
                      <TextFormat type="date" value={loanSubscription.updateAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{loanSubscription.subscriber ? userMap[loanSubscription.subscriber.id] : ''}</td>
                  <td>
                    {loanSubscription.loan ? <Link to={`/loan/${loanSubscription.loan.id}`}>{userMap[loanSubscription.loan.id]}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container"></div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="smartAppleApp.loanSubscription.home.notFound">No Loan Subscriptions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LoanSubscription;
