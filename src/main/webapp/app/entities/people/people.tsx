import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPeople } from 'app/shared/model/people.model';
import { getEntities } from './people.reducer';

export const People = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const peopleList = useAppSelector(state => state.people.entities);
  const loading = useAppSelector(state => state.people.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="people-heading" data-cy="PeopleHeading">
        People
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/people/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new People
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {peopleList && peopleList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>People Id</th>
                <th>Personal Data</th>
                <th>Demographic Data</th>
                <th>Date Of Contact</th>
                <th>Employee</th>
                <th>Reason</th>
                <th>Description</th>
                <th>Action</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {peopleList.map((people, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/people/${people.id}`} color="link" size="sm">
                      {people.id}
                    </Button>
                  </td>
                  <td>{people.peopleId}</td>
                  <td>{people.personalData}</td>
                  <td>{people.demographicData}</td>
                  <td>{people.dateOfContact ? <TextFormat type="date" value={people.dateOfContact} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{people.employee}</td>
                  <td>{people.reason}</td>
                  <td>{people.description}</td>
                  <td>{people.action}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/people/${people.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/people/${people.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/people/${people.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No People found</div>
        )}
      </div>
    </div>
  );
};

export default People;
