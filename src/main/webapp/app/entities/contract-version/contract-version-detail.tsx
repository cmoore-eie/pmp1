import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './contract-version.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ContractVersionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const contractVersionEntity = useAppSelector(state => state.contractVersion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contractVersionDetailsHeading">ContractVersion</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{contractVersionEntity.id}</dd>
          <dt>
            <span id="effectiveDate">Effective Date</span>
          </dt>
          <dd>
            {contractVersionEntity.effectiveDate ? (
              <TextFormat value={contractVersionEntity.effectiveDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expirationDate">Expiration Date</span>
          </dt>
          <dd>
            {contractVersionEntity.expirationDate ? (
              <TextFormat value={contractVersionEntity.expirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="hiddenContract">Hidden Contract</span>
          </dt>
          <dd>{contractVersionEntity.hiddenContract ? 'true' : 'false'}</dd>
          <dt>
            <span id="versionNumber">Version Number</span>
          </dt>
          <dd>{contractVersionEntity.versionNumber}</dd>
          <dt>Contract</dt>
          <dd>{contractVersionEntity.contract ? contractVersionEntity.contract.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/contract-version" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contract-version/${contractVersionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContractVersionDetail;
