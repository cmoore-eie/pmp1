import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './virtual-product-seasoning.reducer';
import { IVirtualProductSeasoning } from 'app/shared/model/virtual-product-seasoning.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductSeasoning = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const virtualProductSeasoningList = useAppSelector(state => state.virtualProductSeasoning.entities);
  const loading = useAppSelector(state => state.virtualProductSeasoning.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="virtual-product-seasoning-heading" data-cy="VirtualProductSeasoningHeading">
        Virtual Product Seasonings
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Virtual Product Seasoning
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {virtualProductSeasoningList && virtualProductSeasoningList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ancestor Item Identifier</th>
                <th>Code</th>
                <th>Condition</th>
                <th>Default Seasoning</th>
                <th>Item Identifier</th>
                <th>Name</th>
                <th>Priority</th>
                <th>Virtual Product Flavour</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {virtualProductSeasoningList.map((virtualProductSeasoning, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${virtualProductSeasoning.id}`} color="link" size="sm">
                      {virtualProductSeasoning.id}
                    </Button>
                  </td>
                  <td>{virtualProductSeasoning.ancestorItemIdentifier}</td>
                  <td>{virtualProductSeasoning.code}</td>
                  <td>{virtualProductSeasoning.condition}</td>
                  <td>{virtualProductSeasoning.defaultSeasoning ? 'true' : 'false'}</td>
                  <td>{virtualProductSeasoning.itemIdentifier}</td>
                  <td>{virtualProductSeasoning.name}</td>
                  <td>{virtualProductSeasoning.priority}</td>
                  <td>
                    {virtualProductSeasoning.virtualProductFlavour ? (
                      <Link to={`virtual-product-flavour/${virtualProductSeasoning.virtualProductFlavour.id}`}>
                        {virtualProductSeasoning.virtualProductFlavour.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductSeasoning.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductSeasoning.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductSeasoning.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Virtual Product Seasonings found</div>
        )}
      </div>
    </div>
  );
};

export default VirtualProductSeasoning;
