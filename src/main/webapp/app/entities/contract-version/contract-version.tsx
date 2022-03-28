import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './contract-version.reducer';
import { IContractVersion } from 'app/shared/model/contract-version.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ContractVersion = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const contractVersionList = useAppSelector(state => state.contractVersion.entities);
  const loading = useAppSelector(state => state.contractVersion.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="contract-version-heading" data-cy="ContractVersionHeading">
        Contract Versions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Contract Version
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contractVersionList && contractVersionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Effective Date</th>
                <th>Expiration Date</th>
                <th>Hidden Contract</th>
                <th>Version Number</th>
                <th>Contract</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contractVersionList.map((contractVersion, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${contractVersion.id}`} color="link" size="sm">
                      {contractVersion.id}
                    </Button>
                  </td>
                  <td>
                    {contractVersion.effectiveDate ? (
                      <TextFormat type="date" value={contractVersion.effectiveDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {contractVersion.expirationDate ? (
                      <TextFormat type="date" value={contractVersion.expirationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{contractVersion.hiddenContract ? 'true' : 'false'}</td>
                  <td>{contractVersion.versionNumber}</td>
                  <td>
                    {contractVersion.contract ? (
                      <Link to={`contract/${contractVersion.contract.id}`}>{contractVersion.contract.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${contractVersion.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${contractVersion.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${contractVersion.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Contract Versions found</div>
        )}
      </div>
    </div>
  );
};

export default ContractVersion;
