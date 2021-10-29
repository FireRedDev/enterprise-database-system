import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITargetSystemCredentials, TargetSystemCredentials } from '../target-system-credentials.model';

import { TargetSystemCredentialsService } from './target-system-credentials.service';

describe('TargetSystemCredentials Service', () => {
  let service: TargetSystemCredentialsService;
  let httpMock: HttpTestingController;
  let elemDefault: ITargetSystemCredentials;
  let expectedResult: ITargetSystemCredentials | ITargetSystemCredentials[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TargetSystemCredentialsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      username: 'AAAAAAA',
      password: 'AAAAAAA',
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

    it('should create a TargetSystemCredentials', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TargetSystemCredentials()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TargetSystemCredentials', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          username: 'BBBBBB',
          password: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TargetSystemCredentials', () => {
      const patchObject = Object.assign(
        {
          username: 'BBBBBB',
          password: 'BBBBBB',
        },
        new TargetSystemCredentials()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TargetSystemCredentials', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          username: 'BBBBBB',
          password: 'BBBBBB',
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

    it('should delete a TargetSystemCredentials', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTargetSystemCredentialsToCollectionIfMissing', () => {
      it('should add a TargetSystemCredentials to an empty array', () => {
        const targetSystemCredentials: ITargetSystemCredentials = { id: 123 };
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing([], targetSystemCredentials);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetSystemCredentials);
      });

      it('should not add a TargetSystemCredentials to an array that contains it', () => {
        const targetSystemCredentials: ITargetSystemCredentials = { id: 123 };
        const targetSystemCredentialsCollection: ITargetSystemCredentials[] = [
          {
            ...targetSystemCredentials,
          },
          { id: 456 },
        ];
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing(
          targetSystemCredentialsCollection,
          targetSystemCredentials
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TargetSystemCredentials to an array that doesn't contain it", () => {
        const targetSystemCredentials: ITargetSystemCredentials = { id: 123 };
        const targetSystemCredentialsCollection: ITargetSystemCredentials[] = [{ id: 456 }];
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing(
          targetSystemCredentialsCollection,
          targetSystemCredentials
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetSystemCredentials);
      });

      it('should add only unique TargetSystemCredentials to an array', () => {
        const targetSystemCredentialsArray: ITargetSystemCredentials[] = [{ id: 123 }, { id: 456 }, { id: 72401 }];
        const targetSystemCredentialsCollection: ITargetSystemCredentials[] = [{ id: 123 }];
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing(
          targetSystemCredentialsCollection,
          ...targetSystemCredentialsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const targetSystemCredentials: ITargetSystemCredentials = { id: 123 };
        const targetSystemCredentials2: ITargetSystemCredentials = { id: 456 };
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing([], targetSystemCredentials, targetSystemCredentials2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetSystemCredentials);
        expect(expectedResult).toContain(targetSystemCredentials2);
      });

      it('should accept null and undefined values', () => {
        const targetSystemCredentials: ITargetSystemCredentials = { id: 123 };
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing([], null, targetSystemCredentials, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetSystemCredentials);
      });

      it('should return initial array if no TargetSystemCredentials is added', () => {
        const targetSystemCredentialsCollection: ITargetSystemCredentials[] = [{ id: 123 }];
        expectedResult = service.addTargetSystemCredentialsToCollectionIfMissing(targetSystemCredentialsCollection, undefined, null);
        expect(expectedResult).toEqual(targetSystemCredentialsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
