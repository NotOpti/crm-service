import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './people.reducer';

export const PeopleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const peopleEntity = useAppSelector(state => state.people.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="peopleDetailsHeading">People</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{peopleEntity.id}</dd>
          <dt>
            <span id="peopleId">People Id</span>
          </dt>
          <dd>{peopleEntity.peopleId}</dd>
          <dt>
            <span id="personalData">Personal Data</span>
          </dt>
          <dd>{peopleEntity.personalData}</dd>
          <dt>
            <span id="demographicData">Demographic Data</span>
          </dt>
          <dd>{peopleEntity.demographicData}</dd>
          <dt>
            <span id="dateOfContact">Date Of Contact</span>
          </dt>
          <dd>
            {peopleEntity.dateOfContact ? <TextFormat value={peopleEntity.dateOfContact} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="employee">Employee</span>
          </dt>
          <dd>{peopleEntity.employee}</dd>
          <dt>
            <span id="reason">Reason</span>
          </dt>
          <dd>{peopleEntity.reason}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{peopleEntity.description}</dd>
          <dt>
            <span id="action">Action</span>
          </dt>
          <dd>{peopleEntity.action}</dd>
        </dl>
        <Button tag={Link} to="/people" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/people/${peopleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PeopleDetail;
