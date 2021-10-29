import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITargetSystemCredentials } from '../target-system-credentials.model';

@Component({
  selector: 'jhi-target-system-credentials-detail',
  templateUrl: './target-system-credentials-detail.component.html',
})
export class TargetSystemCredentialsDetailComponent implements OnInit {
  targetSystemCredentials: ITargetSystemCredentials | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ targetSystemCredentials }) => {
      this.targetSystemCredentials = targetSystemCredentials;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
