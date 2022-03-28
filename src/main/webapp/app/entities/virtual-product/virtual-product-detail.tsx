import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductEntity = useAppSelector(state => state.virtualProduct.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductDetailsHeading">VirtualProduct</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductEntity.id}</dd>
          <dt>
            <span id="allowAffinity">Allow Affinity</span>
          </dt>
          <dd>{virtualProductEntity.allowAffinity ? 'true' : 'false'}</dd>
          <dt>
            <span id="allowCampaign">Allow Campaign</span>
          </dt>
          <dd>{virtualProductEntity.allowCampaign ? 'true' : 'false'}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{virtualProductEntity.code}</dd>
          <dt>
            <span id="effectiveDate">Effective Date</span>
          </dt>
          <dd>
            {virtualProductEntity.effectiveDate ? (
              <TextFormat value={virtualProductEntity.effectiveDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expirationDate">Expiration Date</span>
          </dt>
          <dd>
            {virtualProductEntity.expirationDate ? (
              <TextFormat value={virtualProductEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="locked">Locked</span>
          </dt>
          <dd>{virtualProductEntity.locked ? 'true' : 'false'}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{virtualProductEntity.name}</dd>
          <dt>
            <span id="productCode">Product Code</span>
          </dt>
          <dd>{virtualProductEntity.productCode}</dd>
          <dt>
            <span id="virtualProductType">Virtual Product Type</span>
          </dt>
          <dd>{virtualProductEntity.virtualProductType}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product/${virtualProductEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductDetail;
