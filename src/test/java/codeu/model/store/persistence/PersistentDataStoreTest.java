package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Image;
import codeu.model.data.Message;
import codeu.model.data.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.time.Instant;
import java.util.Set;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PersistentDataStore. The PersistentDataStore class relies on DatastoreService,
 * which in turn relies on being deployed in an AppEngine context. Since this test doesn't run in
 * AppEngine, we use LocalServiceTestHelper to do all of the AppEngine setup so we can test. More
 * info: https://cloud.google.com/appengine/docs/standard/java/tools/localunittesting
 */
public class PersistentDataStoreTest {

  private PersistentDataStore persistentDataStore;
  private final LocalServiceTestHelper appEngineTestHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setup() {
    appEngineTestHelper.setUp();
    persistentDataStore = new PersistentDataStore();
  }

  @After
  public void tearDown() {
    appEngineTestHelper.tearDown();
  }

  @Test
  public void testSaveAndLoadUsers() throws PersistentDataStoreException {
    UUID idOne = UUID.randomUUID();
    String nameOne = "test_username_one";
    String passwordOne = "password1";
    Instant creationOne = Instant.ofEpochMilli(1000);
    int messageCountOne = 10;
    boolean adminOne = false;
    User inputUserOne = new User(idOne, nameOne, passwordOne, creationOne, messageCountOne, adminOne);

    UUID idTwo = UUID.randomUUID();
    String nameTwo = "test_username_two";
    String passwordTwo = "password2";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    int messageCountTwo = 20;
    boolean adminTwo = true;
    User inputUserTwo = new User(idTwo, nameTwo, passwordTwo, creationTwo, messageCountTwo, adminTwo);

    // save
    persistentDataStore.writeThrough(inputUserOne);
    persistentDataStore.writeThrough(inputUserTwo);

    // load
    List<User> resultUsers = persistentDataStore.loadUsers();


    // confirm that what we saved matches what we loaded
    User resultUserOne = resultUsers.get(0);
    Assert.assertEquals(idOne, resultUserOne.getId());
    Assert.assertEquals(nameOne, resultUserOne.getName());
    Assert.assertEquals(passwordOne, resultUserOne.getPassword());
    Assert.assertEquals(creationOne, resultUserOne.getCreationTime());
    Assert.assertEquals(10, resultUserOne.getMessageCount());
    Assert.assertEquals(false, resultUserOne.isAdmin());

    User resultUserTwo = resultUsers.get(1);
    Assert.assertEquals(idTwo, resultUserTwo.getId());
    Assert.assertEquals(nameTwo, resultUserTwo.getName());
    Assert.assertEquals(passwordTwo, resultUserTwo.getPassword());
    Assert.assertEquals(creationTwo, resultUserTwo.getCreationTime());
    Assert.assertEquals(20, resultUserTwo.getMessageCount());
    Assert.assertEquals(true, resultUserTwo.isAdmin());

  }

  @Test
  public void testSaveAndLoadConversations() throws PersistentDataStoreException {
    UUID idOne = UUID.randomUUID();
    UUID ownerOne = UUID.randomUUID();
    String titleOne = "Test_Title";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Conversation inputConversationOne = new Conversation(idOne, ownerOne, titleOne, creationOne);

    UUID idTwo = UUID.randomUUID();
    UUID ownerTwo = UUID.randomUUID();
    String titleTwo = "Test_Title_Two";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Conversation inputConversationTwo = new Conversation(idTwo, ownerTwo, titleTwo, creationTwo);

    // save
    persistentDataStore.writeThrough(inputConversationOne);
    persistentDataStore.writeThrough(inputConversationTwo);

    // load
    List<Conversation> resultConversations = persistentDataStore.loadConversations();

    // confirm that what we saved matches what we loaded
    Conversation resultConversationOne = resultConversations.get(0);
    Assert.assertEquals(idOne, resultConversationOne.getId());
    Assert.assertEquals(ownerOne, resultConversationOne.getOwnerId());
    Assert.assertEquals(titleOne, resultConversationOne.getTitle());
    Assert.assertEquals(creationOne, resultConversationOne.getCreationTime());

    Conversation resultConversationTwo = resultConversations.get(1);
    Assert.assertEquals(idTwo, resultConversationTwo.getId());
    Assert.assertEquals(ownerTwo, resultConversationTwo.getOwnerId());
    Assert.assertEquals(titleTwo, resultConversationTwo.getTitle());
    Assert.assertEquals(creationTwo, resultConversationTwo.getCreationTime());

  }

  @Test
  public void testSaveAndLoadMessages() throws PersistentDataStoreException {
    UUID idOne = UUID.randomUUID();
    UUID conversationOne = UUID.randomUUID();
    UUID authorOne = UUID.randomUUID();
    String contentOne = "test content one";
    Instant creationOne = Instant.ofEpochMilli(1000);
    Message inputMessageOne =
        new Message(idOne, conversationOne, authorOne, contentOne, creationOne, false);

    UUID idTwo = UUID.randomUUID();
    UUID conversationTwo = UUID.randomUUID();
    UUID authorTwo = UUID.randomUUID();
    String contentTwo = "test content one";
    Instant creationTwo = Instant.ofEpochMilli(2000);
    Message inputMessageTwo =
        new Message(idTwo, conversationTwo, authorTwo, contentTwo, creationTwo, false);

    // save
    persistentDataStore.writeThrough(inputMessageOne);
    persistentDataStore.writeThrough(inputMessageTwo);

    // load
    List<Message> resultMessages = persistentDataStore.loadMessages();

    // confirm that what we saved matches what we loaded
    Message resultMessageOne = resultMessages.get(0);
    Assert.assertEquals(idOne, resultMessageOne.getId());
    Assert.assertEquals(conversationOne, resultMessageOne.getConversationId());
    Assert.assertEquals(authorOne, resultMessageOne.getAuthorId());
    Assert.assertEquals(contentOne, resultMessageOne.getContent());
    Assert.assertEquals(creationOne, resultMessageOne.getCreationTime());

    Message resultMessageTwo = resultMessages.get(1);
    Assert.assertEquals(idTwo, resultMessageTwo.getId());
    Assert.assertEquals(conversationTwo, resultMessageTwo.getConversationId());
    Assert.assertEquals(authorTwo, resultMessageTwo.getAuthorId());
    Assert.assertEquals(contentTwo, resultMessageTwo.getContent());
    Assert.assertEquals(creationTwo, resultMessageTwo.getCreationTime());

  }

  @Test 
  public void testSaveAndLoadImages() throws PersistentDataStoreException {
    String [] descriptionsArr1 = {"one", "two", "three", "four"};
    String url = "http://test.jpg";
    Image image1 = new Image(url);
    Image resultImage1 = new Image(url);

    for(int i = 0; i < descriptionsArr1.length; i++)
        image1.addDescription(descriptionsArr1[i]);
    
    persistentDataStore.writeThrough(image1);

    try{
      resultImage1 = persistentDataStore.loadImage(image1.getUrl());
    } catch(Exception e) {
      Assert.fail("Exception thrown when loading image from persistent store.");
    }

    Assert.assertNotNull(resultImage1);

    Set<String> descriptions = resultImage1.getDescription();
    Assert.assertTrue(descriptions.size() == descriptionsArr1.length);
    for(int i = 0; i < descriptionsArr1.length; i++) {
      Assert.assertTrue(descriptions.contains(descriptionsArr1[i]));
    }
  }
}
