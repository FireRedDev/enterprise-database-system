import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TargetsystemcredentialsComponent } from '../list/targetsystemcredentials.component';
import { TargetsystemcredentialsDetailComponent } from '../detail/targetsystemcredentials-detail.component';
import { TargetsystemcredentialsUpdateComponent } from '../update/targetsystemcredentials-update.component';
import { TargetsystemcredentialsRoutingResolveService } from './targetsystemcredentials-routing-resolve.service';

const targetsystemcredentialsRoute: Routes = [
  {
    path: '',
    component: TargetsystemcredentialsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TargetsystemcredentialsDetailComponent,
    resolve: {
      targetsystemcredentials: TargetsystemcredentialsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TargetsystemcredentialsUpdateComponent,
    resolve: {
      targetsystemcredentials: TargetsystemcredentialsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TargetsystemcredentialsUpdateComponent,
    resolve: {
      targetsystemcredentials: TargetsystemcredentialsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(targetsystemcredentialsRoute)],
  exports: [RouterModule],
})
export class TargetsystemcredentialsRoutingModule {}
