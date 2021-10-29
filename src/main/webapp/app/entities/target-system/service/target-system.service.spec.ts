import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITargetSystem, TargetSystem } from '../target-system.model';

import { TargetSystemService } from './target-system.service';

describe('TargetSystem Service', () => {
  let service: TargetSystemService;
  let httpMock: HttpTestingController;
  let elemDefault: ITargetSystem;
  let expectedResult: ITargetSystem | ITargetSystem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TargetSystemService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TargetSystem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TargetSystem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TargetSystem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TargetSystem', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new TargetSystem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TargetSystem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TargetSystem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTargetSystemToCollectionIfMissing', () => {
      it('should add a TargetSystem to an empty array', () => {
        const targetSystem: ITargetSystem = { id: 123 };
        expectedResult = service.addTargetSystemToCollectionIfMissing([], targetSystem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetSystem);
      });

      it('should not add a TargetSystem to an array that contains it', () => {
        const targetSystem: ITargetSystem = { id: 123 };
        const targetSystemCollection: ITargetSystem[] = [
          {
            ...targetSystem,
          },
          { id: 456 },
        ];
        expectedResult = service.addTargetSystemToCollectionIfMissing(targetSystemCollection, targetSystem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TargetSystem to an array that doesn't contain it", () => {
        const targetSystem: ITargetSystem = { id: 123 };
        const targetSystemCollection: ITargetSystem[] = [{ id: 456 }];
        expectedResult = service.addTargetSystemToCollectionIfMissing(targetSystemCollection, targetSystem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetSystem);
      });

      it('should add only unique TargetSystem to an array', () => {
        const targetSystemArray: ITargetSystem[] = [{ id: 123 }, { id: 456 }, { id: 79888 }];
        const targetSystemCollection: ITargetSystem[] = [{ id: 123 }];
        expectedResult = service.addTargetSystemToCollectionIfMissing(targetSystemCollection, ...targetSystemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const targetSystem: ITargetSystem = { id: 123 };
        const targetSystem2: ITargetSystem = { id: 456 };
        expectedResult = service.addTargetSystemToCollectionIfMissing([], targetSystem, targetSystem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetSystem);
        expect(expectedResult).toContain(targetSystem2);
      });

      it('should accept null and undefined values', () => {
        const targetSystem: ITargetSystem = { id: 123 };
        expectedResult = service.addTargetSystemToCollectionIfMissing([], null, targetSystem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetSystem);
      });

      it('should return initial array if no TargetSystem is added', () => {
        const targetSystemCollection: ITargetSystem[] = [{ id: 123 }];
        expectedResult = service.addTargetSystemToCollectionIfMissing(targetSystemCollection, undefined, null);
        expect(expectedResult).toEqual(targetSystemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
