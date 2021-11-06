import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'systemuser',
        data: { pageTitle: 'Systemusers' },
        loadChildren: () => import('./systemuser/systemuser.module').then(m => m.SystemuserModule),
      },
      {
        path: 'targetsystem',
        data: { pageTitle: 'Targetsystems' },
        loadChildren: () => import('./targetsystem/targetsystem.module').then(m => m.TargetsystemModule),
      },
      {
        path: 'targetsystemcredentials',
        data: { pageTitle: 'Targetsystemcredentials' },
        loadChildren: () => import('./targetsystemcredentials/targetsystemcredentials.module').then(m => m.TargetsystemcredentialsModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'Departments' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
