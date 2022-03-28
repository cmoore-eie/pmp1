import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product-flavour.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductFlavourDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductFlavourEntity = useAppSelector(state => state.virtualProductFlavour.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductFlavourDetailsHeading">VirtualProductFlavour</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductFlavourEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{virtualProductFlavourEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{virtualProductFlavourEntity.code}</dd>
          <dt>
            <span id="condition">Condition</span>
          </dt>
          <dd>{virtualProductFlavourEntity.condition}</dd>
          <dt>
            <span id="defaultFlavour">Default Flavour</span>
          </dt>
          <dd>{virtualProductFlavourEntity.defaultFlavour ? 'true' : 'false'}</dd>
          <dt>
            <span id="effectiveDate">Effective Date</span>
          </dt>
          <dd>
            {virtualProductFlavourEntity.effectiveDate ? (
              <TextFormat value={virtualProductFlavourEntity.effectiveDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expirationDate">Expiration Date</span>
          </dt>
          <dd>
            {virtualProductFlavourEntity.expirationDate ? (
              <TextFormat value={virtualProductFlavourEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="grandfathering">Grandfathering</span>
          </dt>
          <dd>{virtualProductFlavourEntity.grandfathering}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{virtualProductFlavourEntity.itemIdentifier}</dd>
          <dt>
            <span id="lineCode">Line Code</span>
          </dt>
          <dd>{virtualProductFlavourEntity.lineCode}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{virtualProductFlavourEntity.name}</dd>
          <dt>
            <span id="priority">Priority</span>
          </dt>
          <dd>{virtualProductFlavourEntity.priority}</dd>
          <dt>
            <span id="rank">Rank</span>
          </dt>
          <dd>{virtualProductFlavourEntity.rank}</dd>
          <dt>Category</dt>
          <dd>{virtualProductFlavourEntity.category ? virtualProductFlavourEntity.category.id : ''}</dd>
          <dt>Virtual Product</dt>
          <dd>{virtualProductFlavourEntity.virtualProduct ? virtualProductFlavourEntity.virtualProduct.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product-flavour" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product-flavour/${virtualProductFlavourEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductFlavourDetail;
