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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
