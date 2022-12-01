import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate, useResolvedPath } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILoan } from 'app/shared/model/loan.model';
import { getEntities } from './loan.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';

export const Loan = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const loanList = useAppSelector(state => state.loan.entities);
  const loading = useAppSelector(state => state.loan.loading);
  const account = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const [userMap, setUserMap] = useState({});
  console.log(users);

  useEffect(() => {
    dispatch(getUsers({}));
    dispatch(getEntities({}));
  }, []);

  useEffect(() => {
    let obj = {};
    users.forEach(element => {
      obj[element.id] = element.login;
    });
    setUserMap(obj);
  }, [users]);
  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="loan-heading" data-cy="LoanHeading">
        <Translate contentKey="smartAppleApp.loan.home.title">Loans</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartAppleApp.loan.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/loan/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="smartAppleApp.loan.home.createLabel">Create new Loan</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {loanList && loanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="smartAppleApp.loan.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.hash">Hash</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.loanAmt">Loan Amt</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.availableAmt">Available Amt</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.interestRate">Interest Rate</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.terms">Terms</Translate>
                </th>
                {/* <th>
                  <Translate contentKey="smartAppleApp.loan.createAt">Create At</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.updateAt">Update At</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.currency">Currency</Translate>
                </th> */}
                <th>
                  <Translate contentKey="smartAppleApp.loan.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="smartAppleApp.loan.owner">Owner</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {loanList.map((loan, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/loan/${loan.id}`} color="link" size="sm">
                      {loan.id}
                    </Button>
                  </td>
                  <td>{loan.hash}</td>
                  <td>{loan.loanAmt}</td>
                  <td>{loan.availableAmt}</td>
                  <td>{loan.interestRate}</td>
                  <td>{loan.terms}</td>
                  {/* <td>{loan.createAt ? <TextFormat type="date" value={loan.createAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{loan.updateAt ? <TextFormat type="date" value={loan.updateAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`smartAppleApp.CURRENCY.${loan.currency}`} />
                  </td> */}
                  <td>
                    <Translate contentKey={`smartAppleApp.LoanStatus.${loan.status}`} />
                  </td>
                  <td>{loan.owner ? userMap[loan.owner.id] : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/* <Button tag={Link} to={`/loan/${loan.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button> */}
                      {loan.status === 'PENDING' ? (
                        <Button tag={Link} to={`/loan-subscription/${loan.id}/new`} color="primary" size="sm" data-cy="entityEditButton">
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.subscript">Buy</Translate>
                          </span>
                        </Button>
                      ) : null}
                      {/* / && loan.owner === account.id */}
                      {loan.status === 'ACTIVE' ? (
                        <Button tag={Link} to={`/payment/${loan.id}/new`} color="info" size="sm" data-cy="entityEditButton">
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.payment">payment</Translate>
                          </span>
                        </Button>
                      ) : null}
                      {/* <Button tag={Link} to={`/loan/${loan.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button> */}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="smartAppleApp.loan.home.notFound">No Loans found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Loan;
