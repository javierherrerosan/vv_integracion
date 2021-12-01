package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestValidUser {
	@Mock
	private static AuthDAO mockAuthDao;
	@Mock
	private static GenericDAO mockGenericDao;
	@Test
	public void testStartRemoteSystemWithValidUserAndSystem() throws OperationNotSupportedException, SystemManagerException {
		User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		String validId = "12345"; // id valido de sistema
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);
		// primero debe ejecutarse la llamada al dao de autenticación
		// despues el de  acceso a datos del sistema (la validaciones del orden en cada prueba)
		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		// instanciamos el manager con los mock creados
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		// llamada al api a probar
		Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), validId);
		assertEquals(retorno.toString(), "[uno, dos]");
		// vemos si se ejecutan las llamadas a los dao, y en el orden correcto
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
	}
	@Test
	public void testStartRemoteSystemWithValidUserAndInvalidSystem() throws SystemManagerException, OperationNotSupportedException {
		User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		String validId = "12345"; // id valido de sistema

		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(null);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		Collection<Object> retorno = manager.startRemoteSystem(validUser.getId(), validId);

		assertNull(retorno);
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);
	}
	
	 @Test
    public void stopRemoteSystemWithValidUserAndSystem() throws SystemManagerException, OperationNotSupportedException {


        User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
        when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
        String validId = "12345"; // id valido de sistema
        ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
        when(mockGenericDao.getSomeData(validUser, "where id=" + validId)).thenReturn(lista);
        InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
        // instanciamos el manager con los mock creados
        SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
        // llamada al api a probar
        Collection<Object> retorno = manager.stopRemoteSystem(validUser.getId(), validId);
        assertEquals(retorno.toString(), "[uno, dos]");
        // vemos si se ejecutan las llamadas a los dao, y en el orden correcto
        ordered.verify(mockAuthDao).getAuthData(validUser.getId());
        ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + validId);

    }

	 @Test
       public void stopRemoteSystemWithValidUserAndInvalidSystem() throws SystemManagerException, OperationNotSupportedException {


        User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
        when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
        String invalidId = "12345"; // id valido de sistema
        ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
        when(mockGenericDao.getSomeData(validUser, "where id=" + invalidId)).thenReturn(null);
        InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
        // instanciamos el manager con los mock creados
        SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
        // llamada al api a probar
        Collection<Object> retorno = manager.stopRemoteSystem(validUser.getId(), invalidId);
        assertNull(retorno);
        // vemos si se ejecutan las llamadas a los dao, y en el orden correcto
        ordered.verify(mockAuthDao).getAuthData(validUser.getId());
        ordered.verify(mockGenericDao).getSomeData(validUser, "where id=" + invalidId);

    }
	
	@Test
	public void testAddRemoteSystemWithValidUserAndSystem() throws Exception {
		User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		String validId = "12345"; // id valido de sistema
		when(mockGenericDao.updateSomeData(validUser, validId)).thenReturn(true);
		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		manager.addRemoteSystem(validUser.getId(), validId);
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(validUser, validId);
	}
	
	@Test
	public void testAddRemoteSystemWithValidUserAndInvalidSystem() throws Exception {
		User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		String invalidId = "012345"; // id valido de sistema
		when(mockGenericDao.updateSomeData(validUser, invalidId)).thenReturn(false);
		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		assertThrows(com.practica.integracion.manager.SystemManagerException.class, ()->manager.addRemoteSystem(validUser.getId(), invalidId));
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).updateSomeData(validUser, invalidId);
	}
	@Test
	public void testDeleteRemoteSystemWithValidUserAndSystem() throws Exception{
		User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		String validId = "12345"; // id valido de sistema
		when(mockGenericDao.deleteSomeData(validUser, validId)).thenReturn(true);
		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		manager.deleteRemoteSystem(validUser.getId(), validId);
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).deleteSomeData(validUser, validId);
	}
	@Test
	public void testDeleteRemoteSystemWithValidUserAndInvalidSystem() throws Exception {
		User validUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(validUser.getId())).thenReturn(validUser);
		String invalidId = "012345"; // id valido de sistema
		when(mockGenericDao.deleteSomeData(validUser, invalidId)).thenReturn(false);
		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		assertThrows(com.practica.integracion.manager.SystemManagerException.class, ()->manager.deleteRemoteSystem(validUser.getId(), invalidId));
		ordered.verify(mockAuthDao).getAuthData(validUser.getId());
		ordered.verify(mockGenericDao).deleteSomeData(validUser, invalidId);
	}

}
