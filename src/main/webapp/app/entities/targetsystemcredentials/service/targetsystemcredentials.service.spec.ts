import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITargetsystemcredentials, Targetsystemcredentials } from '../targetsystemcredentials.model';

import { TargetsystemcredentialsService } from './targetsystemcredentials.service';

describe('Targetsystemcredentials Service', () => {
  let service: TargetsystemcredentialsService;
  let httpMock: HttpTestingController;
  let elemDefault: ITargetsystemcredentials;
  let expectedResult: ITargetsystemcredentials | ITargetsystemcredentials[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TargetsystemcredentialsService);
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

    it('should create a Targetsystemcredentials', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Targetsystemcredentials()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Targetsystemcredentials', () => {
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

    it('should partial update a Targetsystemcredentials', () => {
      const patchObject = Object.assign(
        {
          password: 'BBBBBB',
        },
        new Targetsystemcredentials()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Targetsystemcredentials', () => {
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

    it('should delete a Targetsystemcredentials', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTargetsystemcredentialsToCollectionIfMissing', () => {
      it('should add a Targetsystemcredentials to an empty array', () => {
        const targetsystemcredentials: ITargetsystemcredentials = { id: 123 };
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing([], targetsystemcredentials);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetsystemcredentials);
      });

      it('should not add a Targetsystemcredentials to an array that contains it', () => {
        const targetsystemcredentials: ITargetsystemcredentials = { id: 123 };
        const targetsystemcredentialsCollection: ITargetsystemcredentials[] = [
          {
            ...targetsystemcredentials,
          },
          { id: 456 },
        ];
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing(
          targetsystemcredentialsCollection,
          targetsystemcredentials
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Targetsystemcredentials to an array that doesn't contain it", () => {
        const targetsystemcredentials: ITargetsystemcredentials = { id: 123 };
        const targetsystemcredentialsCollection: ITargetsystemcredentials[] = [{ id: 456 }];
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing(
          targetsystemcredentialsCollection,
          targetsystemcredentials
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetsystemcredentials);
      });

      it('should add only unique Targetsystemcredentials to an array', () => {
        const targetsystemcredentialsArray: ITargetsystemcredentials[] = [{ id: 123 }, { id: 456 }, { id: 7341 }];
        const targetsystemcredentialsCollection: ITargetsystemcredentials[] = [{ id: 123 }];
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing(
          targetsystemcredentialsCollection,
          ...targetsystemcredentialsArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const targetsystemcredentials: ITargetsystemcredentials = { id: 123 };
        const targetsystemcredentials2: ITargetsystemcredentials = { id: 456 };
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing([], targetsystemcredentials, targetsystemcredentials2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(targetsystemcredentials);
        expect(expectedResult).toContain(targetsystemcredentials2);
      });

      it('should accept null and undefined values', () => {
        const targetsystemcredentials: ITargetsystemcredentials = { id: 123 };
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing([], null, targetsystemcredentials, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(targetsystemcredentials);
      });

      it('should return initial array if no Targetsystemcredentials is added', () => {
        const targetsystemcredentialsCollection: ITargetsystemcredentials[] = [{ id: 123 }];
        expectedResult = service.addTargetsystemcredentialsToCollectionIfMissing(targetsystemcredentialsCollection, undefined, null);
        expect(expectedResult).toEqual(targetsystemcredentialsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
