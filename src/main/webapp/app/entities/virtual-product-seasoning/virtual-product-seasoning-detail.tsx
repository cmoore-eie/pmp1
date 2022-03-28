import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './virtual-product-seasoning.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductSeasoningDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const virtualProductSeasoningEntity = useAppSelector(state => state.virtualProductSeasoning.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="virtualProductSeasoningDetailsHeading">VirtualProductSeasoning</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.id}</dd>
          <dt>
            <span id="ancestorItemIdentifier">Ancestor Item Identifier</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.ancestorItemIdentifier}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.code}</dd>
          <dt>
            <span id="condition">Condition</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.condition}</dd>
          <dt>
            <span id="defaultSeasoning">Default Seasoning</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.defaultSeasoning ? 'true' : 'false'}</dd>
          <dt>
            <span id="itemIdentifier">Item Identifier</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.itemIdentifier}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.name}</dd>
          <dt>
            <span id="priority">Priority</span>
          </dt>
          <dd>{virtualProductSeasoningEntity.priority}</dd>
          <dt>Virtual Product Flavour</dt>
          <dd>{virtualProductSeasoningEntity.virtualProductFlavour ? virtualProductSeasoningEntity.virtualProductFlavour.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/virtual-product-seasoning" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/virtual-product-seasoning/${virtualProductSeasoningEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VirtualProductSeasoningDetail;
