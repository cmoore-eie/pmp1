import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './virtual-product.reducer';
import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { VirtualProductType } from 'app/shared/model/enumerations/virtual-product-type.model';

export const VirtualProductUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const virtualProductEntity = useAppSelector(state => state.virtualProduct.entity);
  const loading = useAppSelector(state => state.virtualProduct.loading);
  const updating = useAppSelector(state => state.virtualProduct.updating);
  const updateSuccess = useAppSelector(state => state.virtualProduct.updateSuccess);
  const virtualProductTypeValues = Object.keys(VirtualProductType);
  const handleClose = () => {
    props.history.push('/virtual-product');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...virtualProductEntity,
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
      ? {}
      : {
          virtualProductType: 'OPEN',
          ...virtualProductEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="pmp1App.virtualProduct.home.createOrEditLabel" data-cy="VirtualProductCreateUpdateHeading">
            Create or edit a VirtualProduct
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
                <ValidatedField name="id" required readOnly id="virtual-product-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Allow Affinity"
                id="virtual-product-allowAffinity"
                name="allowAffinity"
                data-cy="allowAffinity"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Allow Campaign"
                id="virtual-product-allowCampaign"
                name="allowCampaign"
                data-cy="allowCampaign"
                check
                type="checkbox"
              />
              <ValidatedField label="Code" id="virtual-product-code" name="code" data-cy="code" type="text" />
              <ValidatedField
                label="Effective Date"
                id="virtual-product-effectiveDate"
                name="effectiveDate"
                data-cy="effectiveDate"
                type="date"
              />
              <ValidatedField
                label="Expiration Date"
                id="virtual-product-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="date"
              />
              <ValidatedField label="Locked" id="virtual-product-locked" name="locked" data-cy="locked" check type="checkbox" />
              <ValidatedField label="Name" id="virtual-product-name" name="name" data-cy="name" type="text" />
              <ValidatedField label="Product Code" id="virtual-product-productCode" name="productCode" data-cy="productCode" type="text" />
              <ValidatedField
                label="Virtual Product Type"
                id="virtual-product-virtualProductType"
                name="virtualProductType"
                data-cy="virtualProductType"
                type="select"
              >
                {virtualProductTypeValues.map(virtualProductType => (
                  <option value={virtualProductType} key={virtualProductType}>
                    {virtualProductType}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/virtual-product" replace color="info">
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

export default VirtualProductUpdate;
