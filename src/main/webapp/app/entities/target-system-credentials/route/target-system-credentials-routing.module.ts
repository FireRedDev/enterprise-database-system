import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TargetSystemCredentialsComponent } from '../list/target-system-credentials.component';
import { TargetSystemCredentialsDetailComponent } from '../detail/target-system-credentials-detail.component';
import { TargetSystemCredentialsUpdateComponent } from '../update/target-system-credentials-update.component';
import { TargetSystemCredentialsRoutingResolveService } from './target-system-credentials-routing-resolve.service';

const targetSystemCredentialsRoute: Routes = [
  {
    path: '',
    component: TargetSystemCredentialsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TargetSystemCredentialsDetailComponent,
    resolve: {
      targetSystemCredentials: TargetSystemCredentialsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TargetSystemCredentialsUpdateComponent,
    resolve: {
      targetSystemCredentials: TargetSystemCredentialsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TargetSystemCredentialsUpdateComponent,
    resolve: {
      targetSystemCredentials: TargetSystemCredentialsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(targetSystemCredentialsRoute)],
  exports: [RouterModule],
})
export class TargetSystemCredentialsRoutingModule {}
