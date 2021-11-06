import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystemuser } from '../systemuser.model';

@Component({
  selector: 'jhi-systemuser-detail',
  templateUrl: './systemuser-detail.component.html',
})
export class SystemuserDetailComponent implements OnInit {
  systemuser: ISystemuser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemuser }) => {
      this.systemuser = systemuser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
