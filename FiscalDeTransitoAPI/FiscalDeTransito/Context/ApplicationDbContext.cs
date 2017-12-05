using FiscalDeTransitoApi.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;

namespace FiscalDeTransitoApi.Context
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext()
            : base()
        {

        }

        public ApplicationDbContext(DbContextOptions pOptions)
            : base(pOptions)
        {

        }

        public static String GetConnection()
        {
            var sqlStringBuilder = new SqlConnectionStringBuilder();
            sqlStringBuilder.DataSource = "localhost";
            sqlStringBuilder.UserID = "sa";
            sqlStringBuilder.Password = "abcde123";
            sqlStringBuilder.InitialCatalog = "FiscalDeTransito";
            return sqlStringBuilder.ToString();
        }

        public DbSet<Infracao> Infracoes { get; set; }
        public DbSet<InfracaoFile> Files { get; set; }
    }
}
