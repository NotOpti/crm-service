import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPeople } from 'app/shared/model/people.model';
import { getEntity, updateEntity, createEntity, reset } from './people.reducer';

export const PeopleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const peopleEntity = useAppSelector(state => state.people.entity);
  const loading = useAppSelector(state => state.people.loading);
  const updating = useAppSelector(state => state.people.updating);
  const updateSuccess = useAppSelector(state => state.people.updateSuccess);

  const handleClose = () => {
    navigate('/people');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateOfContact = convertDateTimeToServer(values.dateOfContact);

    const entity = {
      ...peopleEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateOfContact: displayDefaultDateTime(),
        }
      : {
          ...peopleEntity,
          dateOfContact: convertDateTimeFromServer(peopleEntity.dateOfContact),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.people.home.createOrEditLabel" data-cy="PeopleCreateUpdateHeading">
            Create or edit a People
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="people-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="People Id"
                id="people-peopleId"
                name="peopleId"
                data-cy="peopleId"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Personal Data" id="people-personalData" name="personalData" data-cy="personalData" type="text" />
              <ValidatedField
                label="Demographic Data"
                id="people-demographicData"
                name="demographicData"
                data-cy="demographicData"
                type="text"
              />
              <ValidatedField
                label="Date Of Contact"
                id="people-dateOfContact"
                name="dateOfContact"
                data-cy="dateOfContact"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Employee" id="people-employee" name="employee" data-cy="employee" type="text" />
              <ValidatedField label="Reason" id="people-reason" name="reason" data-cy="reason" type="text" />
              <ValidatedField label="Description" id="people-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Action" id="people-action" name="action" data-cy="action" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/people" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PeopleUpdate;
