import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product-organization.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductOrganizationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductOrganizationEntity = useAppSelector(state => state.virtualProductOrganization.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductOrganizationDetailsHeading">VirtualProductOrganization</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductOrganizationEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{virtualProductOrganizationEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{virtualProductOrganizationEntity.itemIdentifier}</dd>
          <dt>
            <span id="organization">Organization</span>
          </dt>
          <dd>{virtualProductOrganizationEntity.organization}</dd>
          <dt>
            <span id="producerCode">Producer Code</span>
          </dt>
          <dd>{virtualProductOrganizationEntity.producerCode}</dd>
          <dt>Virtual Product</dt>
          <dd>{virtualProductOrganizationEntity.virtualProduct ? virtualProductOrganizationEntity.virtualProduct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product-organization" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product-organization/${virtualProductOrganizationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductOrganizationDetail;
