import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IVirtualProductFlavour } from 'app/shared/model/virtual-product-flavour.model';
import { getEntities as getVirtualProductFlavours } from 'app/entities/virtual-product-flavour/virtual-product-flavour.reducer';
import { getEntity, updateEntity, createEntity, reset } from './virtual-product-seasoning.reducer';
import { IVirtualProductSeasoning } from 'app/shared/model/virtual-product-seasoning.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductSeasoningUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const virtualProductFlavours = useAppSelector(state => state.virtualProductFlavour.entities);
  const virtualProductSeasoningEntity = useAppSelector(state => state.virtualProductSeasoning.entity);
  const loading = useAppSelector(state => state.virtualProductSeasoning.loading);
  const updating = useAppSelector(state => state.virtualProductSeasoning.updating);
  const updateSuccess = useAppSelector(state => state.virtualProductSeasoning.updateSuccess);
  const handleClose = () => {
    props.history.push('/virtual-product-seasoning');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getVirtualProductFlavours({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...virtualProductSeasoningEntity,
      ...values,
      virtualProductFlavour: virtualProductFlavours.find(it => it.id.toString() === values.virtualProductFlavour.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...virtualProductSeasoningEntity,
          virtualProductFlavour: virtualProductSeasoningEntity?.virtualProductFlavour?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.virtualProductSeasoning.home.createOrEditLabel" data-cy="VirtualProductSeasoningCreateUpdateHeading">
            Create or edit a VirtualProductSeasoning
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="virtual-product-seasoning-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Ancestor Item Identifier"
                id="virtual-product-seasoning-ancestorItemIdentifier"
                name="ancestorItemIdentifier"
                data-cy="ancestorItemIdentifier"
                type="text"
              />
              <ValidatedField label="Code" id="virtual-product-seasoning-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Condition" id="virtual-product-seasoning-condition" name="condition" data-cy="condition" type="text" />
              <ValidatedField
                label="Default Seasoning"
                id="virtual-product-seasoning-defaultSeasoning"
                name="defaultSeasoning"
                data-cy="defaultSeasoning"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Item Identifier"
                id="virtual-product-seasoning-itemIdentifier"
                name="itemIdentifier"
                data-cy="itemIdentifier"
                type="text"
              />
              <ValidatedField label="Name" id="virtual-product-seasoning-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Priority" id="virtual-product-seasoning-priority" name="priority" data-cy="priority" type="text" />
              <ValidatedField
                id="virtual-product-seasoning-virtualProductFlavour"
                name="virtualProductFlavour"
                data-cy="virtualProductFlavour"
                label="Virtual Product Flavour"
                type="select"
                required
              >
                <option value="" key="0" />
                {virtualProductFlavours
                  ? virtualProductFlavours.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/virtual-product-seasoning" replace color="info">
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

export default VirtualProductSeasoningUpdate;
