jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITargetSystem, TargetSystem } from '../target-system.model';
import { TargetSystemService } from '../service/target-system.service';

import { TargetSystemRoutingResolveService } from './target-system-routing-resolve.service';

describe('TargetSystem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TargetSystemRoutingResolveService;
  let service: TargetSystemService;
  let resultTargetSystem: ITargetSystem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TargetSystemRoutingResolveService);
    service = TestBed.inject(TargetSystemService);
    resultTargetSystem = undefined;
  });

  describe('resolve', () => {
    it('should return ITargetSystem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTargetSystem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTargetSystem).toEqual({ id: 123 });
    });

    it('should return new ITargetSystem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTargetSystem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTargetSystem).toEqual(new TargetSystem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TargetSystem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTargetSystem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTargetSystem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
